package io.ballerina.mi.analyzer;

import io.ballerina.compiler.api.impl.symbols.BallerinaUnionTypeSymbol;
import io.ballerina.compiler.api.symbols.ParameterSymbol;
import io.ballerina.compiler.api.symbols.TypeDescKind;
import io.ballerina.compiler.api.symbols.TypeSymbol;
import io.ballerina.mi.connectorModel.FunctionParam;
import io.ballerina.mi.connectorModel.UnionFunctionParam;
import io.ballerina.mi.util.Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class ParamFactory {


    /**
     * Create FunctionParam from ParameterSymbol.
     * @param parameterSymbol
     * @param index
     * @return FunctionParam
     */
    public static Optional<FunctionParam> createFunctionParam(ParameterSymbol parameterSymbol, int index) {
        TypeSymbol typeSymbol = parameterSymbol.typeDescriptor();
        TypeDescKind actualTypeKind = Utils.getActualTypeKind(typeSymbol);
        String paramType = Utils.getParamTypeName(actualTypeKind);

        if (paramType != null) {
            if (actualTypeKind == TypeDescKind.UNION) {
                return createUnionFunctionParam(parameterSymbol, index);
            }
            FunctionParam functionParam = new FunctionParam(Integer.toString(index), parameterSymbol.getName().orElseThrow(), paramType);
            functionParam.setParamKind(parameterSymbol.paramKind());
            functionParam.setTypeSymbol(typeSymbol);
            return Optional.of(functionParam);
        }
        return Optional.empty();
    }

    private static Optional<FunctionParam> createUnionFunctionParam(ParameterSymbol parameterSymbol, int index) {

        String paramName = parameterSymbol.getName().orElseThrow();
        UnionFunctionParam functionParam = new UnionFunctionParam(Integer.toString(index), paramName, TypeDescKind.UNION.getName());
        functionParam.setParamKind(parameterSymbol.paramKind());
        functionParam.setTypeSymbol(parameterSymbol.typeDescriptor());
        populateUnionMemberParams(paramName, (BallerinaUnionTypeSymbol) parameterSymbol.typeDescriptor(), functionParam);
        return Optional.of(functionParam);
    }

    private static void populateUnionMemberParams(String paramName, BallerinaUnionTypeSymbol ballerinaUnionTypeSymbol, UnionFunctionParam functionParam) {
        int memberIndex= 0 ;
        for (TypeSymbol memberTypeSymbol : ballerinaUnionTypeSymbol.memberTypeDescriptors()) {
            TypeDescKind actualTypeKind = Utils.getActualTypeKind(memberTypeSymbol);
            String paramType = Utils.getParamTypeName(actualTypeKind);
            if (paramType != null) {
                if (TypeDescKind.NIL.getName().equals(paramType)) {
                    functionParam.setRequired(false);
                } else {
                    String memberParamName = paramName + StringUtils.capitalize(paramType);
                    FunctionParam memberParam = new FunctionParam(Integer.toString(memberIndex), memberParamName, paramType);
                    memberParam.setTypeSymbol(memberTypeSymbol);
                    memberParam.setEnableCondition("[{\"" + paramName + "DataType\": \"" + paramType + "\"}]");
                    functionParam.addUnionMemberParam(memberParam);
                    memberIndex++;
                }
            }
        }
    }
}
