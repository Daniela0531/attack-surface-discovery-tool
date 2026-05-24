import React from 'react';
import Tree from 'rc-tree';
import 'rc-tree/assets/index.css';

// Преобразуем вашу структуру в формат, понятный rc-tree
const convertToTreeData = (node, parentKey = '') => {
  const operationType = node.type ? node.type : 'unnamed';

  // Переносим генерацию ключа вверх, чтобы он был доступен везде
  const key = `${parentKey}-${operationType}-${Date.now()}-${Math.random()}`;

  let renderTitle;

  switch (operationType) {
    case 'METHOD_PARAM': {
      // Используем className вместо зарезервированного слова class
      const className = node.class ? node.class : '';
      const methodSignature = node.methodSignature ? node.methodSignature : '';
      const from_variable = node.from_variable ? node.from_variable : '';
      const to_param = node.to_param ? node.to_param : '';

      renderTitle = (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '4px', alignItems: 'left'}}>
          <span style={{ fontWeight: '600', color: '#1a202c' }}>
            {operationType}
          </span>
          {className && (
            <span style={{ color: '#718096', fontSize: '12px', fontStyle: 'italic' }}>
              class :: {className}
            </span>
          )}
          {methodSignature && (
            <span style={{ color: '#718096', fontSize: '12px', fontStyle: 'italic' }}>
              method :: {methodSignature}
            </span>
          )}
          {from_variable && (
            <span style={{ color: '#718096', fontSize: '12px', fontStyle: 'italic' }}>
              from variable :: {from_variable}
            </span>
          )}
          {to_param && (
            <span style={{ color: '#718096', fontSize: '12px', fontStyle: 'italic' }}>
              to param :: {to_param}
            </span>
          )}
        </div>
      );
      break;
    }
    case 'ASSIGNMENT': {
      // Используем className вместо зарезервированного слова class
      const from_variable = node.from_variable ? node.from_variable : '';
      const to_param = node.to_param ? node.to_param : '';

      renderTitle = (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '4px', alignItems: 'left'}}>
          <span style={{ fontWeight: '600', color: '#1a202c' }}>
            {operationType}
          </span>
          {from_variable && (
            <span style={{ color: '#718096', fontSize: '12px', fontStyle: 'italic' }}>
              from variable :: {from_variable}
            </span>
          )}
          {to_param && (
            <span style={{ color: '#718096', fontSize: '12px', fontStyle: 'italic' }}>
              to param :: {to_param}
            </span>
          )}
        </div>
      );
      break;
    }
    default:
      // Фолбек для остальных типов операций, чтобы title не был пустым
      renderTitle = <span>{operationType}</span>;
      break;
  }

  const result = {
    title: renderTitle,
    key: key,
  };

  if (node.children && node.children.length > 0) {
    result.children = node.children.map((child, index) =>
      convertToTreeData(child, `${key}-${index}`)
    );
  }

  return result;
};

const TreeView = ({ jsonData }) => {
  if (!jsonData || !jsonData.children) {
    return <div>Нет данных для отображения</div>;
  }

  const treeData = jsonData.children.map((child, index) =>
    convertToTreeData(child, `root-${index}`)
  );

  const customStyles = {
    background: 'white',
    padding: '20px',
    borderRadius: '12px',
    boxShadow: '0 10px 40px rgba(0,0,0,0.1)',
    fontSize: '14px',
    fontFamily: 'system-ui, -apple-system, sans-serif'
  };

  return (
    <div style={customStyles}>
      <h2 style={{ marginBottom: '20px', color: '#333', borderBottom: '2px solid #667eea', paddingBottom: '10px' }}>
        📁 Трассы
      </h2>
      <Tree
        treeData={treeData}
        defaultExpandAll={false}
        defaultExpandParent={true}
        showLine={true}
        showIcon={true}
        selectable={true}

        // 1. Стили для адаптивной высоты и выравнивания элементов внутри дерева
        style={{
          fontSize: '14px',
          lineHeight: 'normal' // Сбрасываем фиксированную высоту строки библиотеки
        }}

        // 2. Внедряем CSS-фикс для внутренних классов rc-tree прямо в рантайме
        className="adaptive-tree"

        icon={({ isLeaf }) => {
          if (isLeaf) {
            return (
              <span style={{
                fontSize: '18px',
                color: '#333333',
                display: 'inline-block',
                alignSelf: 'flex-start', // Чтобы точка оставалась вверху при длинном тексте
                marginTop: '-2px',       // Легкая корректировка положения точки
                marginRight: '4px'
              }}>
                •
              </span>
            );
          }
          return null;
        }}
      />

      {/* Глобальные стили для переопределения внутренней структуры rc-tree */}
      <style>{`
        /* Делаем строки дерева флекс-контейнерами с автоматической высотой */
        .adaptive-tree .rc-tree-treenode {
          display: flex !important;
          align-items: flex-start !important; /* Выравнивает иконки и текст по верхней линии */
          height: auto !important;
          padding: 6px 0 !important; /* Комфортные отступы между многострочными нодами */
        }

        /* Контейнер самого текста (title) теперь занимает всю оставшуюся ширину и переносится */
        .adaptive-tree .rc-tree-node-content-wrapper {
          height: auto !important;
          white-space: normal !important; /* Разрешаем перенос длинного текста на новые строки */
          word-break: break-word;        /* Защита от слишком длинных слов */
          display: inline-block !important;
        }

        /* Выравниваем служебные элементы (стрелочки switcher и линии) по верху */
        .adaptive-tree .rc-tree-switcher,
        .adaptive-tree .rc-tree-iconEle {
          flex-shrink: 0;
          align-self: flex-start !important;
        }
      `}</style>
    </div>
  );
};

export default TreeView;

//import React from 'react';
//import Tree from 'rc-tree';
//import 'rc-tree/assets/index.css';
//
//// Преобразуем вашу структуру в формат, понятный rc-tree
//const convertToTreeData = (node, parentKey = '') => {
//  // Имя узла - первый элемент из nameList
//  const nodeName = node.type
//    ? node.type
//    : 'unnamed';
//
//  // Создаём уникальный ключ
//  const key = `${parentKey}-${nodeName}-${Date.now()}-${Math.random()}`;
//
//  const result = {
//    title: nodeName,
//    key: key,
//  };
//
//  // Если есть дети, рекурсивно обрабатываем их
//  if (node.children && node.children.length > 0) {
//    result.children = node.children.map((child, index) =>
//      convertToTreeData(child, `${key}-${index}`)
//    );
//  }
//
//  return result;
//};
//
//const TreeView = ({ jsonData }) => {
//  if (!jsonData || !jsonData.children) {
//    return <div>Нет данных для отображения</div>;
//  }
//
//  // Преобразуем данные
//  const treeData = jsonData.children.map((child, index) =>
//    convertToTreeData(child, `root-${index}`)
//  );
//
//  // Стили для кастомного оформления
//  const customStyles = {
//    background: 'white',
//    padding: '20px',
//    borderRadius: '12px',
//    boxShadow: '0 10px 40px rgba(0,0,0,0.2)',
//    fontSize: '14px',
//  };
//
//  return (
//    <div style={customStyles}>
//      <h2 style={{ marginBottom: '20px', color: '#333', borderBottom: '2px solid #667eea', paddingBottom: '10px' }}>
//        📁 Трассы
//      </h2>
//      <Tree
//        treeData={treeData}
//        defaultExpandAll={false}
//        defaultExpandParent={true}
//        showLine={true}
//        showIcon={true}
//        selectable={true}
//        style={{ fontSize: '14px' }}
//
//        // отображение своих иконок:
//        icon={({ isLeaf }) => {
//          if (isLeaf) {
//            return <span>⭐</span>; // Ваша звёздочка для файлов (конечных узлов)
//          }
//          // Для папок (узлов с детьми) ничего не возвращаем,
//          // чтобы они сохранили свой дефолтный вид папок
//          return null;
//        }}
//      />
//    </div>
//  );
//};
//
//export default TreeView;