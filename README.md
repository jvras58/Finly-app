# 💰 Finanças Pessoais App

Aplicativo Android para gerenciamento de finanças pessoais, desenvolvido com **Kotlin**, **Jetpack Compose** e arquitetura baseada em **MVVM**.  
Este repositório é estruturado para garantir escalabilidade, separação de responsabilidades e facilidade de manutenção.

---

## 📦 Estrutura do Projeto

```text
app/
├── build.gradle.kts
├── google-services.json
├── proguard-rules.pro
└── src/
    └── main/
        ├── AndroidManifest.xml
        ├── java/
        │   └── com.equipealpha.financaspessoais/
        │       ├── MainActivity.kt
        │       ├── data/               ← 📂 Data Layer
        │       │   ├── entities/       ← Models e entidades de banco
        │       │   ├── local/          ← Room (DAO, Database)
        │       │   ├── migrations/     ← Migrations do banco
        │       │   └── repository/     ← Implementação de repositórios
        │       ├── navigation/         ← Lógica de navegação central
        │       ├── ui/                 ← 📂 View Layer
        │       │   ├── cadastro/, dashboard/, login/, etc.
        │       │   ├── theme/          ← Tema (cores, fontes, formas)
        │       │   └── navigation/     ← Bottom Navigation
        │       └── viewmodel/          ← 📂 ViewModel Layer (MVVM)
        │           ├── AuthViewModel.kt
        │           ├── TransactionViewModel.kt
        │           └── ...
        └── res/                        ← Recursos (imagens, layouts XML)
```

---

## 🧱 Arquitetura

A arquitetura do projeto segue o padrão **MVVM (Model-View-ViewModel)**:

- **Model (Data Layer):**  
  Responsável por armazenar e gerenciar os dados da aplicação (Room, entidades, repositórios, migrations).

- **View (UI Layer):**  
  Composta por telas em Jetpack Compose, responsável por exibir os dados ao usuário e capturar suas interações.

- **ViewModel:**  
  Atua como intermediário entre a View e o Model, mantendo o estado da interface e processando a lógica de negócios.

---

## 🛠️ Tecnologias Utilizadas

- Kotlin
- Jetpack Compose
- Room (SQLite)
- MVVM
- Navigation Component
- Material 3
- Gradle Kotlin DSL
- Firebase (Google Services)

---

## 🚀 Como rodar

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/financas-pessoais.git
   ```
2. Abra no Android Studio.
3. Adicione seu `google-services.json` na pasta `app/`.
4. Rode o projeto no emulador ou dispositivo físico.


## 📄 Licença

Este projeto está licenciado sob a [MIT License](LICENSE).
