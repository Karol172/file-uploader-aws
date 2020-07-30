import React from 'react';
import logo from './logo.svg';
import './App.css';
import Item from './Item/Item'


function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />

        <Item id="1" filename="Example-file.js" size="128" />
      </header>
    </div>
  );
}

export default App;
