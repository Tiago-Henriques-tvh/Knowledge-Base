# 🧱 **Programação Orientada a Objetos (OOP)**

OOP é um **paradigma de programação** que organiza sistemas em **objetos**, combinando **dados** (atributos/propriedades) e **comportamentos** (métodos).

- **Objetos** são instâncias de **classes**.
- **Classes** são “blueprints” que definem a estrutura e comportamento dos objetos.

---

## 🔹 **Conceitos fundamentais**

| Conceito           | Explicação                                                                    |
| ------------------ | ----------------------------------------------------------------------------- |
| **Classe**         | Modelo que define atributos e métodos de um objeto.                           |
| **Objeto**         | Instância de uma classe, com valores concretos.                               |
| **Propriedades**   | Dados armazenados dentro de um objeto.                                        |
| **Métodos**        | Comportamentos de um objeto, ações que ele pode realizar.                     |
| **Encapsulamento** | Controle de acesso aos dados, protegendo propriedades internas.               |
| **Herança**        | Permite que uma classe herde atributos e métodos de outra.                    |
| **Polimorfismo**   | Um objeto pode ter múltiplas formas ou comportamentos dependendo do contexto. |
| **Abstração**      | Esconder detalhes internos, mostrando apenas a interface necessária.          |
| **Interface**      | Contrato que define métodos que uma classe deve implementar.                  |
| **Construtor**     | Inicializa objetos quando criados.                                            |
| **Destruidor**     | Liberar recursos quando um objeto deixa de ser usado.                         |

---

## 🔹 **Pilares da OOP**

### 1️⃣ Encapsulamento

- Controla o acesso aos dados do objeto.
- Mantém dados privados e expõe apenas métodos públicos ou propriedades seguras.
- Exemplo de uso: um objeto “Conta Bancária” protege saldo, permitindo depósitos e saques apenas por métodos.

### 2️⃣ Herança

- Cria hierarquias de classes, **reutilizando código**.
- Exemplo de uso: “Cachorro” e “Gato” herdam de “Animal”, reutilizando métodos como “comer” ou “dormir”.

### 3️⃣ Polimorfismo

- Permite que diferentes objetos respondam de maneira diferente à mesma mensagem/método.
- Exemplo de uso: um método “desenhar” que desenha círculos, quadrados ou triângulos dependendo do objeto.

### 4️⃣ Abstração

- Esconde complexidade, mostrando apenas a interface necessária.
- Exemplo de uso: “Pagamento” abstrai diferentes métodos de pagamento (cartão, boleto, PIX), mas o sistema só vê a operação “pagar”.

---

## 🔹 **Interfaces e Contratos**

- Define **o que uma classe deve fazer**, sem especificar como.
- Permite múltiplas implementações diferentes.
- Exemplo de uso: “IFlyable” para objetos que podem voar (pássaro, avião), cada um implementa de forma própria.

---

## 🔹 **Composição vs Herança**

- **Herança** → “É um tipo de” (Cachorro é um Animal)
- **Composição** → “Tem um” (Carro tem um Motor)
- Composição é mais flexível e permite trocar componentes sem alterar a hierarquia de classes.

---

## 🔹 **Princípios SOLID**

1. **S – Single Responsibility** → cada classe tem uma única responsabilidade.
2. **O – Open/Closed** → aberto para extensão, fechado para modificação.
3. **L – Liskov Substitution** → subclasses devem substituir a classe base sem quebrar o sistema.
4. **I – Interface Segregation** → interfaces pequenas e específicas.
5. **D – Dependency Inversion** → dependa de abstrações, não de implementações.

---

## 🔹 **Boas práticas OOP**

- Use encapsulamento para proteger dados.
- Prefira composição em vez de herança quando possível.
- Mantenha classes pequenas e com responsabilidade única.
- Use interfaces para dependências e facilitar testes.
- Nomeie classes e métodos de forma clara e significativa.
