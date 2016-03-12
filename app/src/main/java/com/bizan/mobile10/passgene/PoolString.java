package com.bizan.mobile10.passgene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 文字生成のため
 */
public class PoolString {
    private final static String BR = System.getProperty("line.separator");

    public void setElem1S(String elem1S) {
        this.elem1S = elem1S;
    }

    public void setElem1N(String elem1N) {
        this.elem1N = elem1N;
    }

    public void setElem2S(String elem2S) {
        this.elem2S = elem2S;
    }

    public void setElem2N(String elem2N) {
        this.elem2N = elem2N;
    }

    public void setnElemP(String nElemP) {
        this.nElemP = nElemP;
    }

    public void setnElemN(String nElemN) {
        this.nElemN = nElemN;
    }

    public void setnElemS(String nElemS) {
        this.nElemS = nElemS;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public void setUpperN(String upperN) {
        this.upperN.add(upperN);
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }
    private String elem1S = "";
    private String elem1N = "";
    private String elem2S = "";
    private String elem2N = "";

    private String methodType = "";

    private ArrayList<String> upperN;

    private String sign = "";
    private String nElemS = "";
    private String nElemN = "";
    private String nElemP = "";

    private boolean loop = false;
    private String totalNum = "";


    public PoolString() {
        upperN = new ArrayList<String>();
    }

    public void init() {
        elem1S = "";
        elem1N = "";
        elem2S = "";
        elem2N = "";

        methodType = "";

        upperN.clear();

        sign = "";
        nElemS = "";
        nElemN = "";
        nElemP = "";

        loop = false;

        totalNum = "";
    }


    public String makeString() {
        StringBuilder stb = new StringBuilder();
        stb.append("【　ヒント　】");
        stb.append(BR);
        if (elem1S.length() != 0) {
            stb.append(elem1S);
            stb.append(elem1N);
            stb.append("文字");
        }
        if (elem2S.length() != 0) {
            stb.append("　");
            stb.append(elem2S);
            stb.append(elem2N);
            stb.append("文字");
        }
        if (methodType.length() != 0) {
            stb.append("を");
            stb.append(methodType);
            stb.append("後,");
        }
        if (upperN.size() != 0) {
            stb.append("の、頭から");
            for (String s : upperN) {
                stb.append(s);
                stb.append("文字目");
            }
            stb.append("を大文字にし、");
        }
        if(methodType.length() != 0){
            stb.append(methodType);
            stb.append("し");
        }
        if (sign.length() != 0) {
            stb.append(" ");
            stb.append("記号「 ");
            stb.append(sign);
            stb.append(" 」を足し、");
        }
        stb.append(" ");
        stb.append(nElemS);
        stb.append("下");
        stb.append(nElemN);
        stb.append("桁");
        if (nElemP.length() != 0) {
            stb.append("分 各桁に");
            stb.append(nElemP);
            stb.append("を加え");
        }
        stb.append("ました。");
        if (loop) {
            stb.append("以上を");
            stb.append(totalNum);
            stb.append("文字になるまで繰り返します。");
        }
//        String str = "｛要素A○文字｝、｛要素B○文字｝　｛をミックス後｝、｛○文字目を大文字｝にし" +
//                "｛記号○｝をたし　数字要素下○桁に○を加え" +
//                "ました。";
        return stb.toString();
    }


}
