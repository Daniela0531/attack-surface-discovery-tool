import React, { useState, useEffect } from 'react';
import TreeView from './TreeView';

const App = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [pageNum, setPageNum] = useState(0);
  const [pageSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const fetchData = async (page) => {
    setLoading(true);
    setError(null);

    try {
      const response = await fetch(`http://localhost:8080/api/results?pageNum=${page}&pageSize=${pageSize}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
        mode: 'cors',
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();

      // Собираем все данные в одну структуру
      const combinedData = {
        children: result.content.map((item, index) => {
          // Если у item есть поле type, используем его
          // Иначе ищем вложенный type
          const type = item.type ||
                       (item.children && item.children.length > 0 && item.children[0].type) ||
                       'RESULT';

          return {
            ...item,
            type: type,
            id: index,
          };
        })
      };

      setData(combinedData);
      setTotalPages(result.totalPages);
      setTotalElements(result.totalElements);
      setPageNum(result.pageNum);
    } catch (err) {
      setError(err.message);
      console.error('Error fetching data:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData(0);
  }, []);

  const handleNextPage = () => {
    if (pageNum < totalPages - 1) {
      fetchData(pageNum + 1);
    }
  };

  const handlePrevPage = () => {
    if (pageNum > 0) {
      fetchData(pageNum - 1);
    }
  };

  if (loading) {
    return (
      <div style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        background: '#f0f0f0',
        fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
      }}>
        <div style={{
          color: '#555',
          fontSize: '13px',
        }}>
          Loading...
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        background: '#f0f0f0',
        fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
      }}>
        <div style={{
          color: '#c0392b',
          fontSize: '13px',
          maxWidth: '500px',
          textAlign: 'center',
        }}>
          <div style={{ marginBottom: '8px' }}>⚠️ Error</div>
          <div style={{ color: '#555', fontSize: '12px' }}>{error}</div>
          <div style={{ color: '#888', fontSize: '11px', marginTop: '8px' }}>
            Check server at http://localhost:8080
          </div>
        </div>
      </div>
    );
  }

  if (!data || !data.children || data.children.length === 0) {
    return (
      <div style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        background: '#f0f0f0',
        fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
      }}>
        <div style={{
          color: '#555',
          fontSize: '13px',
        }}>
          No data available
        </div>
      </div>
    );
  }

  return (
    <div style={{
      background: '#f0f0f0',
      minHeight: '100vh',
      padding: '0',
      fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
      display: 'flex',
      flexDirection: 'column',
    }}>
      {/* Toolbar */}
      <div style={{
        background: '#f5f5f5',
        padding: '4px 16px',
        borderBottom: '1px solid #d0d0d0',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        minHeight: '36px',
        flexShrink: 0,
      }}>
        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '16px',
          fontSize: '12px',
          color: '#555',
        }}>
          <span style={{ color: '#888' }}>
            Results: <span style={{ color: '#333' }}>{totalElements}</span>
          </span>
          <span style={{ color: '#888' }}>
            Page: <span style={{ color: '#333' }}>{pageNum + 1}</span>
            <span style={{ color: '#888' }}> / {totalPages || 1}</span>
          </span>
        </div>
        <div style={{
          display: 'flex',
          gap: '4px',
        }}>
          <button
            onClick={handlePrevPage}
            disabled={pageNum === 0}
            style={{
              padding: '4px 12px',
              background: 'transparent',
              color: pageNum === 0 ? '#ccc' : '#333',
              border: 'none',
              borderRadius: '2px',
              fontSize: '12px',
              cursor: pageNum === 0 ? 'default' : 'pointer',
              fontFamily: 'inherit',
              transition: 'background 0.15s ease',
            }}
            onMouseEnter={(e) => {
              if (pageNum !== 0) e.target.style.background = '#e0e0e0';
            }}
            onMouseLeave={(e) => {
              e.target.style.background = 'transparent';
            }}
          >
            ←
          </button>
          <button
            onClick={handleNextPage}
            disabled={pageNum >= totalPages - 1 || totalPages === 0}
            style={{
              padding: '4px 12px',
              background: 'transparent',
              color: pageNum >= totalPages - 1 || totalPages === 0 ? '#ccc' : '#333',
              border: 'none',
              borderRadius: '2px',
              fontSize: '12px',
              cursor: pageNum >= totalPages - 1 || totalPages === 0 ? 'default' : 'pointer',
              fontFamily: 'inherit',
              transition: 'background 0.15s ease',
            }}
            onMouseEnter={(e) => {
              if (!(pageNum >= totalPages - 1 || totalPages === 0)) {
                e.target.style.background = '#e0e0e0';
              }
            }}
            onMouseLeave={(e) => {
              e.target.style.background = 'transparent';
            }}
          >
            →
          </button>
        </div>
      </div>

      {/* Основной контент */}
      <div style={{
        flex: 1,
        padding: '16px',
        overflow: 'auto',
        background: '#f0f0f0',
      }}>
        <TreeView jsonData={data} />
      </div>
    </div>
  );
};

export default App;

//import React, { useState, useEffect } from 'react';
//import TreeView from './TreeView';
//import jsonData from '../../result.json';
//
//const App = () => {
//  const [data, setData] = useState(null);
//  const [loading, setLoading] = useState(true);
//
//  useEffect(() => {
//    // Имитация загрузки (можно заменить на fetch, если нужно грузить с сервера)
//    setTimeout(() => {
//      setData(jsonData);
//      setLoading(false);
//    }, 500);
//  }, []);
//
//  if (loading) {
//    return (
//      <div style={{
//        textAlign: 'center',
//        padding: '50px',
//        background: 'white',
//        borderRadius: '12px',
//        boxShadow: '0 10px 40px rgba(0,0,0,0.2)'
//      }}>
//        <h2>⏳ Загрузка дерева...</h2>
//      </div>
//    );
//  }
//
//  return <TreeView jsonData={data} />;
//};
//
//export default App;
