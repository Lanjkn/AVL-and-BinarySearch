class No {

    int dado;           // armazena a chave
    No pai;             // ponteiro para o nó pai
    No esquerda;        // ponteiro para o filho da esquerda
    No direita;         // ponteiro para o filho da direita
    int FB;             // fator de balanço/equilibrio do nó

    public No(int dado) {
        this.dado = dado;
        this.pai = null;
        this.esquerda = null;
        this.direita = null;
        this.FB = 0;
    }
}

public class AVL {

    private No raiz;

    public AVL() {
        this.raiz = null;
    }


    /*imprimir a estrutura em árvore na tela*/
    private void imprimirIndentado(No ptrAtual, String indenta, boolean ultimo) {
        if (ptrAtual != null) {
            System.out.print(indenta);
            if (ultimo) {
                System.out.print("D----");
                indenta += "     ";
            } else {
                System.out.print("E----");
                indenta += "|    ";
            }
            System.out.println(ptrAtual.dado + "(FB = " + ptrAtual.FB + ")");
            imprimirIndentado(ptrAtual.esquerda, indenta, false);
            imprimirIndentado(ptrAtual.direita, indenta, true);
        }
    }

    public int altura(){
        return altura(this.raiz);
    }
    
    public int altura(No no){
        if(no ==null){
            return -1;
        }
        int esquerda =altura(no.esquerda);
        int direita = altura(no.direita);
        if (esquerda > direita){
            return 1 + esquerda;
        }else{
            return 1+ direita;
        }
    }
    
    // buscar na árvore a chave k e retornar o nó correspondente
    public No buscar(int k) {
        return buscar(this.raiz, k);
    }    
    
    /*Busca de uma chave na estrutura de dados -- árvore binária */
    /*Se sucesso ou fracasso retorna o nó atual*/
    private No buscar(No no, int chave) {
        if (no == null || chave == no.dado) {
            return no;
        }

        if (chave < no.dado) {
            return buscar(no.esquerda, chave);
        }
        return buscar(no.direita, chave);
    }

    // Remover o nó (identificado pela chave) da árvore
    No remover(int chave) {
        return remover(this.raiz, chave);
    }
    
    private No remover(No no, int chave) {
        // verifica incialmente se nó raiz (ou no) é null
        if (no == null) {
            return no;
        } else if (chave < no.dado) {
            // processar a subárvore a esquerda
            no.esquerda = remover(no.esquerda, chave);
        } else if (chave > no.dado) {
            // processar a subárvore a direita
            no.direita = remover(no.direita, chave);
        } else {
            // A chave foi encontrada, agora delete-a
            // Caso 1: não nenhum filho
            if (no.esquerda == null && no.direita == null) {
              No buscar  =buscar(no.dado);
              No antecessor =antecessor(buscar);

              if(altura(antecessor) ==2 || altura(antecessor) == -2){
                  atualizarFatorDeEquilibrio(antecessor);
              }

                no = null;
            } // Caso 2: tem apenas um filho
            else if (no.esquerda == null) {
                No temp = no;
                no = no.direita;
            } else if (no.direita == null) {
                No temp = no;
                no = no.esquerda;
            } // Caso 3: Tem os dois filhos
            else {
                No temp = buscarMenorChave(no.direita);
                no.dado = temp.dado;
                no.direita = remover(no.direita, temp.dado);
            }
        }

        return no;
    }

    // Atualizar o fator (FB) de equilibrio/balanço do nó
    private void atualizarFatorDeEquilibrio(No no) {
        if (no.FB < -1 || no.FB > 1) {
            reequilibrar(no);
            return;
        }

        if (no.pai != null) {
            if (no == no.pai.esquerda) {
                no.pai.FB -= 1;
            }

            if (no == no.pai.direita) {
                no.pai.FB += 1;
            }

            if (no.pai.FB != 0) {
                atualizarFatorDeEquilibrio(no.pai);
            }
        }
    }

    // Reequilibrar a árvore
    void reequilibrar(No no) {
        if (no.FB > 0) {
            if (no.direita.FB < 0) {
                rotacaoDireita(no.direita);
                rotacaoEsquerda(no);
            } else {
                rotacaoEsquerda(no);
            }
        } else if (no.FB < 0) {
            if (no.esquerda.FB > 0) {
                rotacaoEsquerda(no.esquerda);
                rotacaoDireita(no);
            } else {
                rotacaoDireita(no);
            }
        }
    }
    
    // Travessia Pre-Ordem
    // Nó . Subárvore Esquerda . Subárvore Direita
    public void preOrdem() {
        preOrdem(this.raiz);
    }

    private void preOrdem(No no) {
        if (no != null) {
            System.out.print(no.dado + " ");
            preOrdem(no.esquerda);
            preOrdem(no.direita);
        }
    }

    // Travessia Em-Ordem
    // Subárvore Esquerda . Nó . Subárvore Direita    
    public void emOrdem() {
        emOrdem(this.raiz);
    }

    private void emOrdem(No no) {
        if (no != null) {
            emOrdem(no.esquerda);
            System.out.print(no.dado + " ");
            emOrdem(no.direita);
        }
    }

    // Travessia pós-Ordem
    // Subárvore Esquerda . Nó . Subárvore Direita    
    public void posOrdem() {
        posOrdem(this.raiz);
    }

    private void posOrdem(No no) {
        if (no != null) {
            posOrdem(no.esquerda);
            posOrdem(no.direita);
            System.out.print(no.dado + " ");
        }
    }

    // encontra o nó com a menor chave
    public No buscarMenorChave(No no) {
        while (no.esquerda != null) {
            no = no.esquerda;
        }
        return no;
    }

    // encontra o nó com a maior chave
    public No buscarMaiorChave(No no) {
        while (no.direita != null) {
            no = no.direita;
        }
        return no;
    }

    // encontrar/buscar o sucessor de um dado nó
    public No sucessor(No x) {
        // Se a subárvore direita não for nula, 
        // o sucessor será o nó mais à esquerda na subárvore direita
        if (x.direita != null) {
            return buscarMenorChave(x.direita);
        }
        // Senão será o menor ancestral de x cujo 
        // filho da esquerda também é um ancestral de x.
        No y = x.pai;
        while (y != null && x == y.direita) {
            x = y;
            y = y.pai;
        }
        return y;
    }

    // encontrar/buscar o antecessor de um dado nó
    public No antecessor(No x) {
        // se a subárvore esquerda não for nula, o antecessor 
        // será o nó mais à direita na subárvore esquerda
        if (x.esquerda != null) {
            return buscarMaiorChave(x.esquerda);
        }
        No y = x.pai;
        while (y != null && x == y.esquerda) {
            x = y;
            y = y.pai;
        }
        return y;
    }

    // Roração a esquerda do nó x
    void rotacaoEsquerda(No x) {
        No y = x.direita;
        x.direita = y.esquerda;
        if (y.esquerda != null) {
            y.esquerda.pai = x;
        }
        y.pai = x.pai;
        if (x.pai == null) {
            this.raiz = y;
        } else if (x == x.pai.esquerda) {
            x.pai.esquerda = y;
        } else {
            x.pai.direita = y;
        }
        y.esquerda = x;
        x.pai = y;

        // atualiza fator de equilibrio/balanceamento
        x.FB = x.FB - 1 - Math.max(0, y.FB);
        y.FB = y.FB - 1 + Math.min(0, x.FB);
    }

    // Roração a direita do nó x
    void rotacaoDireita(No x) {
        No y = x.esquerda;
        x.esquerda = y.direita;
        if (y.direita != null) {
            y.direita.pai = x;
        }
        y.pai = x.pai;
        if (x.pai == null) {
            this.raiz = y;
        } else if (x == x.pai.direita) {
            x.pai.direita = y;
        } else {
            x.pai.esquerda = y;
        }
        y.direita = x;
        x.pai = y;

        // atualiza fator de equilibrio/balanceamento
        x.FB = x.FB + 1 - Math.min(0, y.FB);
        y.FB = y.FB + 1 + Math.max(0, x.FB);
    }

    // inserir a chave da árvore em sua posição apropriada
    public void inserir(int chave) {

        No no = new No(chave);
        No p = null;
        No f = this.raiz;

        // Encontrar a posição para inserior o novo nó/chave
        while (f != null) {
            p = f;
            if (no.dado < f.dado) {
                f = f.esquerda;
            } else {
                f = f.direita;
            }
        }

        // p é o pai de f (que é o filho de p).
        no.pai = p;
        if (p == null) {
            raiz = no;
        } else if (no.dado < p.dado) {
            p.esquerda = no;
        } else {
            p.direita = no;
        }

        // atulizar o fator de equilibrio do nó se necessário
        atualizarFatorDeEquilibrio(no);
    }

    // Imprimir a estrutura em árvore
    public void imprimir() {
        imprimirIndentado(this.raiz, "", true);
    }

    public static void main(String[] args) {
        
        AVL avl = new AVL();
        
        avl.inserir(10);
        avl.inserir(20);
        avl.inserir(30);
        avl.inserir(40);
        avl.inserir(50);
        avl.inserir(60);
        avl.inserir(70);
        avl.inserir(80);

        avl.imprimir();
        System.out.println("Altura: "+avl.altura());

        avl.remover(80);
        avl.imprimir();
        System.out.println("Altura: "+avl.altura());
        
        avl.remover(40);
        avl.imprimir();
        System.out.println("Altura: "+avl.altura());
    }
}
