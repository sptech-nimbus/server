package com.user.user.util;

public class ListaObj<T> {
    private T[] vetor;
    private int nroElem;

    @SuppressWarnings("unchecked")
    public ListaObj(int tamanho) {
        vetor = (T[]) new Object[tamanho];
        nroElem = 0;
    }

    public void add(T elemento) {
        if (nroElem < vetor.length) {
            vetor[nroElem++] = elemento;
        }
    }

    public int busca(T elementoBuscado) {
        for (int i = 0; i < nroElem; i++) {
            if (vetor[i].equals(elementoBuscado)) {
                return i;
            }
        }
        return -1;
    }

    public boolean removePeloIndice(int indice) {
        
        for (int i = indice; i < nroElem; i++) {
            vetor[i] = vetor[i+1];
        }

        nroElem--;
        return true;
    }


    public boolean removeElemento(T elementoARemover) {
        return removePeloIndice(busca(elementoARemover));
    }

    public int getTamanho() {
        return nroElem;
    }

    public T getElemento(int indice) {
        if (indice < 0 || indice >= nroElem) {
            return null;
        }
        else{
            return vetor[indice];
        }
    }

    public void limpa() {
        nroElem = 0;
    }

    public void exibe() {
        if (nroElem == 0) {
            System.out.println("\nA lista está vazia.");
        }
        else {
            System.out.println("\nElementos da lista");
            for (int i = 0; i < nroElem; i++) {
                System.out.println(vetor[i]);
            }
        }
    }

    public T[] getVetor() {
        return vetor;
    }
}
