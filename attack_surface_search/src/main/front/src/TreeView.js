import React from 'react';
import Tree from 'rc-tree';
import 'rc-tree/assets/index.css';

// Преобразуем вашу структуру в формат, понятный rc-tree
const convertToTreeData = (node, parentKey = '') => {
  // Имя узла - первый элемент из nameList
  const nodeName = node.name
    ? node.name
    : 'unnamed';

  // Создаём уникальный ключ
  const key = `${parentKey}-${nodeName}-${Date.now()}-${Math.random()}`;

  const result = {
    title: nodeName,
    key: key,
  };

  // Если есть дети, рекурсивно обрабатываем их
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

  // Преобразуем данные
  const treeData = jsonData.children.map((child, index) =>
    convertToTreeData(child, `root-${index}`)
  );

  // Стили для кастомного оформления
  const customStyles = {
    background: 'white',
    padding: '20px',
    borderRadius: '12px',
    boxShadow: '0 10px 40px rgba(0,0,0,0.2)',
    fontSize: '14px',
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
        style={{ fontSize: '14px' }}
      />
    </div>
  );
};

export default TreeView;