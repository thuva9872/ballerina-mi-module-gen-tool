package io.ballerina.mi.connectorModel;

import java.util.ArrayList;
import java.util.List;

public class UnionFunctionParam extends FunctionParam{

    private List<FunctionParam> unionMemberParams;

    public UnionFunctionParam(String index, String name, String paramType) {
        super(index, name, paramType);
        unionMemberParams = new ArrayList<>();
    }

    public List<FunctionParam> getUnionMemberParams() {
        return unionMemberParams;
    }

    public void setUnionMemberParams(List<FunctionParam> unionMemberParams) {
        this.unionMemberParams = unionMemberParams;
    }

    public void addUnionMemberParam(FunctionParam functionParam) {
        functionParam.setRequired(isRequired());
        this.unionMemberParams.add(functionParam);
    }

    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
        for (FunctionParam memberParam : unionMemberParams) {
            memberParam.setRequired(required);
        }
    }
}
