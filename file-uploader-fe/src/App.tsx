import React from 'react';
import logo from './logo.svg';
import './App.css';
import ItemTable from './ItemTable/ItemTable';


const data = [{filename: "f1.txt", size: 1024},
    {filename: "f2.txt", size: 2},]
function App() {
  return (
    <div className="App">
        <ItemTable items={data} page={1} size={20} />
    </div>
  );
}

export default App;
