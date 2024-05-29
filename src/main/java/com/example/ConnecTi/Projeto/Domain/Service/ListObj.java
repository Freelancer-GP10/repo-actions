package com.example.ConnecTi.Projeto.Domain.Service;

public class ListObj <T>{
    private T[] vetor;
    private int nroElem;

    public ListObj(int tamanho) {
        this.vetor =(T[]) new Object[tamanho];
        this.nroElem = 0;

    }

    public void adicionar(T numero){
        if (vetor.length>nroElem){
            vetor[nroElem++] = numero;
        }
    }
    public void exibir(){
        for (int i =0; i <nroElem;i++){
            System.out.println(vetor[i]);
        }
    }

    public int buscar(T achar){
        for (int i =0; i <vetor.length;i++) {
            if (vetor[i].equals(achar)) {
                return (int) vetor[i];
            }
        }
        return -1;
    }
    public boolean removerPeloIndice(int indice){
        if (indice >=0 && indice <= nroElem){
            for (int i = 0;  i < nroElem - 1; i++){
                vetor[i] = vetor[i + 1];
                System.out.println("REMOVIDOOOOOOOOO");
                nroElem--;
                return true;
            }
        }
        System.out.println("NÂO REMOVEU");
        return false;
    }

    public boolean removeElemento(T elemento) {
        int indice = buscar(elemento);
        if (indice != -1) {
            return removerPeloIndice(indice);
        }
        return false; // Elemento não encontrado
    }
}
