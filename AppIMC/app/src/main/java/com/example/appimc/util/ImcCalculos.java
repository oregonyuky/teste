package com.example.appimc.util;

public class ImcCalculos {
    public static double calcular(int peso, double altura){
        if(altura>0)
            return peso/Math.pow(altura,2);
        return 0;
    }
    public static int pesoPerder(double imc, char sexo, double altura, int peso){
        double imcIdeal=(sexo=='m')?26.7:25.8;
        if(imc>imcIdeal)
            return peso - (int) (imcIdeal*Math.pow(altura,2));
        return 0;
    }
    public static String getCondicaoFisica(double imc, char sexo){
        String cf="abaixo do peso";
        if(sexo=='m'){
            if(imc > 31.1) cf="obeso";
            else if (imc>27.8) cf="acima do peso ideal";
            else if (imc>26.4) cf="marginalmente acima do peso";
            else if (imc>20.7) cf="peso normal";
        }
        else{
            if(imc > 32.3) cf="obeso";
            else if (imc>27.3) cf="acima do peso ideal";
            else if (imc>25.8) cf="marginalmente acima do peso";
            else if (imc>19.1) cf="peso normal";
        }
        return cf;
    }
}
