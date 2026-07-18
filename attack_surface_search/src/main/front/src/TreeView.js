import React from 'react';
import Tree from 'rc-tree';
import 'rc-tree/assets/index.css';

// Проверяем, есть ли у ноды поля для отображения
const hasDisplayFields = (node) => {
  const fields = ['class', 'methodSignature', 'from_variable', 'to_param', 'value', 'name', 'type'];
  return fields.some(field => node[field] && node[field].toString().trim() !== '');
};

// Получаем тип операции или имя ноды
const getNodeType = (node) => {
  // Если есть type - используем его
  if (node.type) return node.type;
  // Если есть operationType - используем его
  if (node.operationType) return node.operationType;
  // Если есть name - используем его
  if (node.name) return node.name;
  // Иначе возвращаем 'Node'
  return 'Node';
};

// Извлекаем все поля из ноды
const extractFields = (node) => {
  const fields = [];
  const excludeFields = ['children', 'type', 'operationType', 'id']; // поля, которые не показываем как отдельные

  Object.keys(node).forEach(key => {
    if (!excludeFields.includes(key) && node[key] && node[key].toString().trim() !== '') {
      fields.push({
        label: key,
        value: node[key]
      });
    }
  });

  return fields;
};

// Преобразуем структуру в формат, понятный rc-tree
const convertToTreeData = (node, parentKey = '') => {
  const nodeType = getNodeType(node);
  const key = `${parentKey}-${nodeType}-${Date.now()}-${Math.random()}`;

  // Получаем все поля для отображения
  const fields = extractFields(node);

  let renderTitle;

  // Если есть поля - отображаем их, иначе просто показываем тип
  if (fields.length > 0) {
    renderTitle = (
      <div style={{
        display: 'flex',
        flexDirection: 'column',
        gap: '2px',
        width: '100%'
      }}>
        <span style={{
          fontWeight: '600',
          color: '#1a202c',
          fontSize: '13px'
        }}>
          {nodeType}
        </span>
        <div style={{
          display: 'flex',
          flexWrap: 'wrap',
          gap: '4px 12px'
        }}>
          {fields.map((field, idx) => (
            <FieldItem
              key={idx}
              label={field.label}
              value={field.value}
            />
          ))}
        </div>
      </div>
    );
  } else {
    renderTitle = (
      <span style={{
        fontWeight: '500',
        color: '#1a202c',
        fontSize: '13px'
      }}>
        {nodeType}
      </span>
    );
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

// Компонент для отображения одного поля
const FieldItem = ({ label, value }) => {
  const [isHovered, setIsHovered] = React.useState(false);

  // Красивое отображение label
  const formatLabel = (label) => {
    // Заменяем подчеркивания на пробелы
    return label.replace(/_/g, ' ');
  };

  return (
    <div
      style={{
        display: 'flex',
        alignItems: 'center',
        gap: '4px',
        fontSize: '12px',
        color: '#4a5568',
        background: '#f7fafc',
        padding: '2px 8px',
        borderRadius: '4px',
        border: '1px solid #e2e8f0',
        maxWidth: '300px',
      }}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      <span style={{
        color: '#718096',
        fontWeight: '500',
        whiteSpace: 'nowrap'
      }}>
        {formatLabel(label)}:
      </span>
      <span
        style={{
          color: '#2d3748',
          overflow: 'hidden',
          textOverflow: 'ellipsis',
          whiteSpace: 'nowrap',
          maxWidth: '200px',
          cursor: 'pointer',
          position: 'relative',
        }}
        title={String(value)}
      >
        {String(value)}
      </span>
      {isHovered && String(value).length > 30 && (
        <div style={{
          position: 'fixed',
          background: '#1a202c',
          color: 'white',
          padding: '8px 12px',
          borderRadius: '6px',
          fontSize: '12px',
          maxWidth: '400px',
          wordBreak: 'break-all',
          zIndex: 1000,
          boxShadow: '0 4px 12px rgba(0,0,0,0.3)',
          pointerEvents: 'none',
          transform: 'translateY(-100%)',
          marginTop: '-8px',
        }}>
          {String(value)}
        </div>
      )}
    </div>
  );
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
      <h2 style={{
        marginBottom: '20px',
        color: '#333',
        borderBottom: '2px solid #667eea',
        paddingBottom: '10px'
      }}>
        📁 Трассы
      </h2>
      <Tree
        treeData={treeData}
        defaultExpandAll={false}
        defaultExpandParent={true}
        showLine={true}
        showIcon={true}
        selectable={true}
        style={{
          fontSize: '14px',
          lineHeight: 'normal'
        }}
        className="adaptive-tree"
        icon={({ isLeaf }) => {
          if (isLeaf) {
            return (
              <span style={{
                fontSize: '18px',
                color: '#333333',
                display: 'inline-block',
                alignSelf: 'flex-start',
                marginTop: '-2px',
                marginRight: '4px'
              }}>
                •
              </span>
            );
          }
          return null;
        }}
      />

      <style>{`
        .adaptive-tree .rc-tree-treenode {
          display: flex !important;
          align-items: flex-start !important;
          height: auto !important;
          padding: 6px 0 !important;
        }

        .adaptive-tree .rc-tree-node-content-wrapper {
          height: auto !important;
          white-space: normal !important;
          word-break: break-word;
          display: inline-block !important;
          width: auto !important;
        }

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
//  const operationType = node.type ? node.type : 'unnamed';
//
//  const key = `${parentKey}-${operationType}-${Date.now()}-${Math.random()}`;
//
//  let renderTitle;
//
//  switch (operationType) {
//    case 'METHOD_PARAM': {
//      const className = node.class ? node.class : '';
//      const methodSignature = node.methodSignature ? node.methodSignature : '';
//      const from_variable = node.from_variable ? node.from_variable : '';
//      const to_param = node.to_param ? node.to_param : '';
//
//      // Собираем все поля в массив для отображения
//      const fields = [
//        { label: 'class', value: className },
//        { label: 'method', value: methodSignature },
//        { label: 'from variable', value: from_variable },
//        { label: 'to param', value: to_param }
//      ];
//
//      renderTitle = (
//        <div style={{ display: 'flex', flexDirection: 'column', gap: '2px' }}>
//          <span style={{ fontWeight: '600', color: '#1a202c' }}>
//            {operationType}
//          </span>
//          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px 16px' }}>
//            {fields.map((field, idx) => {
//              if (!field.value) return null;
//              return (
//                <FieldItem
//                  key={idx}
//                  label={field.label}
//                  value={field.value}
//                />
//              );
//            })}
//          </div>
//        </div>
//      );
//      break;
//    }
//    case 'ASSIGNMENT': {
//      const from_variable = node.from_variable ? node.from_variable : '';
//      const to_param = node.to_param ? node.to_param : '';
//
//      const fields = [
//        { label: 'from variable', value: from_variable },
//        { label: 'to param', value: to_param }
//      ];
//
//      renderTitle = (
//        <div style={{ display: 'flex', flexDirection: 'column', gap: '2px' }}>
//          <span style={{ fontWeight: '600', color: '#1a202c' }}>
//            {operationType}
//          </span>
//          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px 16px' }}>
//            {fields.map((field, idx) => {
//              if (!field.value) return null;
//              return (
//                <FieldItem
//                  key={idx}
//                  label={field.label}
//                  value={field.value}
//                />
//              );
//            })}
//          </div>
//        </div>
//      );
//      break;
//    }
//    default:
//      renderTitle = <span>{operationType}</span>;
//      break;
//  }
//
//  const result = {
//    title: renderTitle,
//    key: key,
//  };
//
//  if (node.children && node.children.length > 0) {
//    result.children = node.children.map((child, index) =>
//      convertToTreeData(child, `${key}-${index}`)
//    );
//  }
//
//  return result;
//};
//
//// Компонент для отображения одного поля с обрезкой текста
//const FieldItem = ({ label, value }) => {
//  const [isHovered, setIsHovered] = React.useState(false);
//
//  return (
//    <div
//      style={{
//        display: 'flex',
//        alignItems: 'center',
//        gap: '4px',
//        fontSize: '12px',
//        color: '#4a5568',
//        background: '#f7fafc',
//        padding: '2px 8px',
//        borderRadius: '4px',
//        border: '1px solid #e2e8f0',
//        maxWidth: '250px',
//      }}
//      onMouseEnter={() => setIsHovered(true)}
//      onMouseLeave={() => setIsHovered(false)}
//    >
//      <span style={{
//        color: '#718096',
//        fontWeight: '500',
//        whiteSpace: 'nowrap'
//      }}>
//        {label}:
//      </span>
//      <span
//        style={{
//          color: '#2d3748',
//          overflow: 'hidden',
//          textOverflow: 'ellipsis',
//          whiteSpace: 'nowrap',
//          maxWidth: '150px',
//          cursor: 'pointer',
//          position: 'relative',
//        }}
//        title={value} // Встроенная подсказка браузера
//      >
//        {value}
//      </span>
//      {/* Кастомная подсказка при наведении */}
//      {isHovered && value.length > 30 && (
//        <div style={{
//          position: 'fixed',
//          background: '#1a202c',
//          color: 'white',
//          padding: '8px 12px',
//          borderRadius: '6px',
//          fontSize: '12px',
//          maxWidth: '400px',
//          wordBreak: 'break-all',
//          zIndex: 1000,
//          boxShadow: '0 4px 12px rgba(0,0,0,0.3)',
//          pointerEvents: 'none',
//          // Позиционируем подсказку над элементом
//          transform: 'translateY(-100%)',
//          marginTop: '-8px',
//        }}>
//          {value}
//        </div>
//      )}
//    </div>
//  );
//};
//
//const TreeView = ({ jsonData }) => {
//  if (!jsonData || !jsonData.children) {
//    return <div>Нет данных для отображения</div>;
//  }
//
//  const treeData = jsonData.children.map((child, index) =>
//    convertToTreeData(child, `root-${index}`)
//  );
//
//  const customStyles = {
//    background: 'white',
//    padding: '20px',
//    borderRadius: '12px',
//    boxShadow: '0 10px 40px rgba(0,0,0,0.1)',
//    fontSize: '14px',
//    fontFamily: 'system-ui, -apple-system, sans-serif'
//  };
//
//  return (
//    <div style={customStyles}>
//      <h2 style={{
//        marginBottom: '20px',
//        color: '#333',
//        borderBottom: '2px solid #667eea',
//        paddingBottom: '10px'
//      }}>
//        📁 Трассы
//      </h2>
//      <Tree
//        treeData={treeData}
//        defaultExpandAll={false}
//        defaultExpandParent={true}
//        showLine={true}
//        showIcon={true}
//        selectable={true}
//        style={{
//          fontSize: '14px',
//          lineHeight: 'normal'
//        }}
//        className="adaptive-tree"
//        icon={({ isLeaf }) => {
//          if (isLeaf) {
//            return (
//              <span style={{
//                fontSize: '18px',
//                color: '#333333',
//                display: 'inline-block',
//                alignSelf: 'flex-start',
//                marginTop: '-2px',
//                marginRight: '4px'
//              }}>
//                •
//              </span>
//            );
//          }
//          return null;
//        }}
//      />
//
//      <style>{`
//        .adaptive-tree .rc-tree-treenode {
//          display: flex !important;
//          align-items: flex-start !important;
//          height: auto !important;
//          padding: 6px 0 !important;
//        }
//
//        .adaptive-tree .rc-tree-node-content-wrapper {
//          height: auto !important;
//          white-space: normal !important;
//          word-break: break-word;
//          display: inline-block !important;
//          width: auto !important;
//        }
//
//        .adaptive-tree .rc-tree-switcher,
//        .adaptive-tree .rc-tree-iconEle {
//          flex-shrink: 0;
//          align-self: flex-start !important;
//        }
//      `}</style>
//    </div>
//  );
//};
//
//export default TreeView;
//
