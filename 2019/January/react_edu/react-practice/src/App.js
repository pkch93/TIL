import React, { Component } from 'react';
import BoxRow from "./BoxRow";
import './app.css'

class App extends Component {
  render() {
    const names = ["kim", "lee", "park", "choi", "yoo", "cho"];
    return (
      <div className="container">
        <BoxRow names={names}/>
        <BoxRow names={names}/>
        <BoxRow names={names}/>
        <BoxRow names={names}/>
        <BoxRow names={names}/>
        <BoxRow names={names}/>
        <BoxRow names={names}/>
      </div>
    );
  }
}

export default App;
