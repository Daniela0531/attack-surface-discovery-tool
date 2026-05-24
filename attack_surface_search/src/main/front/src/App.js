import React, { useState, useEffect } from 'react';
import TreeView from './TreeView';
import jsonData from '../../result.json';

const App = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Имитация загрузки (можно заменить на fetch, если нужно грузить с сервера)
    setTimeout(() => {
      setData(jsonData);
      setLoading(false);
    }, 500);
  }, []);

  if (loading) {
    return (
      <div style={{
        textAlign: 'center',
        padding: '50px',
        background: 'white',
        borderRadius: '12px',
        boxShadow: '0 10px 40px rgba(0,0,0,0.2)'
      }}>
        <h2>⏳ Загрузка дерева...</h2>
      </div>
    );
  }

  return <TreeView jsonData={data} />;
};

export default App;