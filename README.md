# bads-backend
# 1. Git Branching Strategy

## 🔹 Main branches
- main    -> The stable production branch.
- staging -> The stable testing branch.
- develop   -> The main development branch (all features are merged here before going to production)

## 🔹 Supporting Branches
- feature/branch-name   -> For new features
- bugfix/branch-name    -> For fixing bugs
- hotfix/branch-name    -> For urgent product fixes
- release/branch-name   -> For preparing a production release

## 🔹 Branch name convention

```bash
<task_id>-<screen_name | component_name | ...>
```

# 2. Git Commit Message Convention

## 🎯 Commit Message Structure
```bash
<type>:(scope): <message>
```
- type -> The purpose of the commit
- scope (optional) -> The affected module, component, or file
- message -> A short, clear description of the change

## 🛠 Commit Types

| Type              | Usage |
| :---------------- | :--------------------- |
| feat              |  ✨ A new feature   |
| fix               |  🐛 A bug fix   |
| docs              |  📄 Documentation changes   |
| style             |  🎨 Formatting, missing semi-colons, linting   |
| refactor          |  🔨 Code refactoring without functional changes   |
| test              |  ✅ Adding or modifying tests   |
| chore             |  🏗️ Build process or auxiliary tools changes   |
| perf              |  🚀 Performance improvements   |
| ci                |  🛠️ Changes to CI/CD workflows   |
| revert            |  ⏪ Revert a previous commit   |

## ✅ Example Commit Messages

```bash
git commit -m "feat(auth): add user login functionality"
git commit -m "fix(cart): resolve checkout button bug"
git commit -m "docs(readme): update installation guide"
git commit -m "style(header): fix spacing and indentation"
git commit -m "refactor(api): optimize database queries"
```
