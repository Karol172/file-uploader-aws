import React from 'react';
import logo from './logo.svg';
import './App.css';
import TableComponent from "./TableComponent/TableComponent";


function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <TableComponent />
      </header>
    </div>
  );
}

export default App;
