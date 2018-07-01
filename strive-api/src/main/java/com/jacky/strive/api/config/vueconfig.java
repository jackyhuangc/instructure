// {
//     "eslint.autoFixOnSave": true,
//     "editor.formatOnType": true,
//     "editor.formatOnSave": true,
//     "eslint.validate": [
//         "javascript",
//         "javascriptreact",
//         "html",
//         "vue"
//     ]
// }
// 将设置放入此文件中以覆盖默认设置
{
        "editor.fontSize": 16,
        "editor.tabSize": 2,
        "editor.insertSpaces": true,
        "editor.formatOnSave": true,
        "files.associations": {
        "*.vue": "vue"
        },
        "eslint.autoFixOnSave": true,
        "eslint.validate": [
        "javascript",
        "javascriptreact",
        "html",
        "vue",
        {
        "language": "html",
        "autoFix": true
        }
        ],
        "emmet.syntaxProfiles": {
        "vue-html": "html",
        "vue": [
        "css",
        "html",
        "less"
        ]
        },
        "editor.fontFamily": "Source Code Pro, 'Courier New', monospace",
        "files.autoSave": "off",
        "java.errors.incompleteClasspath.severity": "ignore",
        "extensions.ignoreRecommendations": false,
        // 解决vue文件中的html格式失效的问题
        "vetur.format.defaultFormatter.html": "js-beautify-html",
        "search.location": "panel"
        //"workbench.iconTheme": "vscode-icons"
        }