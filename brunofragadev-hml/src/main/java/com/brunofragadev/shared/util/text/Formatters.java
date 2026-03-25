package com.brunofragadev.shared.util.text;

public class Formatters {
    public static String ofuscarEmail(String email) {
        // Retorna o proprio texto se for nulo ou inválido
        if (email == null || !email.contains("@")) {
            return email;
        }

        String[] partes = email.split("@");
        String nomeUsuario = partes[0];
        String dominio = partes[1];

        // Se o e-mail for muito curto (ex: ana@gmail.com), mostra só a 1ª letra
        if (nomeUsuario.length() <= 4) {
            return nomeUsuario.substring(0, 1) + "***@" + dominio;
        }

        // Pega as 4 primeiras letras e concatena com 5 asteriscos
        String prefixo = nomeUsuario.substring(0, 4);
        return prefixo + "*****@" + dominio;
    }
}
