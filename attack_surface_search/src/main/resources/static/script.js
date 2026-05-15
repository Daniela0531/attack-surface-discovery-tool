const express = require('express');
const app = express();
const port = 8080;

// Отдаём статические файлы из папки public
app.use(express.static('public'));

app.get('/', (req, res) => {
    res.send(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Моя картинка</title>
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    min-height: 100vh;
                    background: #1a1a1a;
                }
                img {
                    max-width: 90%;
                    max-height: 90vh;
                    border-radius: 10px;
                    box-shadow: 0 10px 30px rgba(0,0,0,0.5);
                }
            </style>
        </head>
        <body>
            <img src="/my-image.jpg" alt="Моя картинка">
        </body>
        </html>
    `);
});

app.listen(port, () => {
    console.log(`Сервер запущен: http://localhost:${port}`);
});

//let cy;
//
//document.addEventListener('DOMContentLoaded', function() {
//    cy = cytoscape({
//        container: document.getElementById('cy'),
//        style: getDefaultStyle(),
//        layout: {
//            name: 'breadthfirst',
//            directed: true,
//            padding: 10
//        }
//    });
//
//    // Загружаем граф при старте
//    loadFullGraph();
//
//    // Обработчик клика
//    cy.on('tap', 'node', function(evt) {
//        const node = evt.target;
//        showNodeDetails(node.id());
//        highlightNeighbors(node);
//    });
//
//    // Обработчик двойного клика - раскрытие
//    cy.on('dbltap', 'node', function(evt) {
//        const node = evt.target;
//        expandNode(node.id());
//    });
//
//    // Клик на фон - сброс
//    cy.on('tap', function(evt) {
//        if (evt.target === cy) {
//            document.getElementById('node-details').innerHTML =
//                '<p>Кликните на узел для информации. Двойной клик для раскрытия.</p>';
//            cy.elements().removeClass('highlighted');
//        }
//    });
//});
//
//function getDefaultStyle() {
//    return [
//        {
//            selector: 'node',
//            style: {
//                'background-color': '#4CAF50',
//                'label': 'data(label)',
//                'font-size': '12px',
//                'text-valign': 'center',
//                'text-halign': 'center',
//                'width': '60px',
//                'height': '60px',
//                'border-width': 2,
//                'border-color': '#333'
//            }
//        },
//        {
//            selector: 'node[type="METHOD"]',
//            style: { 'background-color': '#66BB6A' }
//        },
//        {
//            selector: 'node[type="CONSTRUCTOR"]',
//            style: { 'background-color': '#42A5F5' }
//        },
//        {
//            selector: 'node[type="INTERFACE_METHOD"]',
//            style: { 'background-color': '#FFA726' }
//        },
//        {
//            selector: 'node.highlighted',
//            style: {
//                'border-color': '#FF0000',
//                'border-width': 4
//            }
//        },
//        {
//            selector: 'edge',
//            style: {
//                'width': 2,
//                'line-color': '#999',
//                'target-arrow-color': '#999',
//                'target-arrow-shape': 'triangle',
//                'curve-style': 'bezier',
//                'label': 'data(label)',
//                'font-size': '10px'
//            }
//        },
//        {
//            selector: 'edge[label="CALLS"]',
//            style: { 'line-color': '#E53935', 'target-arrow-color': '#E53935' }
//        },
//        {
//            selector: 'edge[label="IMPLEMENTS"]',
//            style: { 'line-color': '#7B1FA2', 'target-arrow-color': '#7B1FA2',
//                     'line-style': 'dashed' }
//        },
//        {
//            selector: 'edge[label="CONSTRUCTOR_CALL"]',
//            style: { 'line-color': '#1565C0', 'target-arrow-color': '#1565C0' }
//        }
//    ];
//}
//
//async function loadFullGraph() {
//    try {
//        const response = await fetch('/api/graph');
//        const graph = await response.json();
//        renderGraph(graph);
//    } catch (error) {
//        console.error('Ошибка загрузки графа:', error);
//    }
//}
//
//function renderGraph(graph) {
//    cy.elements().remove();
//
//    // Добавляем узлы
//    graph.nodes.forEach(node => {
//        cy.add({
//            group: 'nodes',
//            data: {
//                id: node.id,
//                label: node.label,
//                type: node.type,
//                fullName: node.fullName
//            }
//        });
//    });
//
//    // Добавляем рёбра
//    graph.edges.forEach(edge => {
//        cy.add({
//            group: 'edges',
//            data: {
//                id: edge.from + '_' + edge.to + '_' + Math.random(),
//                source: edge.from,
//                target: edge.to,
//                label: edge.label
//            }
//        });
//    });
//
//    cy.layout({ name: 'breadthfirst', directed: true }).run();
//    document.getElementById('info').textContent =
//        `Узлов: ${graph.nodes.length}, Рёбер: ${graph.edges.length}`;
//}
//
////async function showNodeDetails(nodeId) {
////    try {
////        const response = await fetch('/api/node/' + nodeId);
////        const details = await response.json();
////
////        let html = '<table>';
////        html += `<tr><td>ID:</td><td>${details.node.id}</td></tr>`;
////        html += `<tr><td>Метка:</td><td>${details.node.label}</td></tr>`;
////        html += `<tr><td>Тип:</td><td>${details.node.type}</td></tr>`;
////        html += `<tr><td>Полное имя:</td><td>${details.node.fullName}</td></tr>`;
////
////        if (details.incoming && details.incoming.length > 0) {
////            html += `<tr><td>Входящие вызовы:</td><td>${details.incoming.length}</td></tr>`;
////        }
////        if (details.outgoing && details.outgoing.length > 0) {
////            html += `<tr><td>Исходящие вызовы:</td><td>${details.outgoing.length}</td></tr>`;
////        }
////
////        html += '</table>';
////        document.getElementById('node-details').innerHTML = html;
////    } catch (error) {
////        console.error('Ошибка:', error);
////    }
////}
//
//function highlightNeighbors(selectedNode) {
//    cy.elements().removeClass('highlighted');
//    selectedNode.addClass('highlighted');
//
//    selectedNode.connectedEdges().forEach(edge => {
//        edge.addClass('highlighted');
//        edge.connectedNodes().forEach(node => {
//            node.addClass('highlighted');
//        });
//    });
//}
//
////async function expandNode(nodeId) {
////    try {
////        const response = await fetch('/api/expand/' + nodeId);
////        const expanded = await response.json();
////        renderGraph(expanded);
////        document.getElementById('info').textContent += ' (раскрыто)';
////    } catch (error) {
////        console.error('Ошибка:', error);
////    }
////}
//
//function fitGraph() {
//    cy.fit();
//}
//
////function resetGraph() {
////    loadFullGraph();
////    document.getElementById('node-details').innerHTML =
////        '<p>Кликните на узел для информации. Двойной клик для раскрытия.</p>';
////}