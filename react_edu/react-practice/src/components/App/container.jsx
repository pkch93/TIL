import React, { Component } from 'react';
import App from './presenter';

class Container extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <App />
    );
  }
}

export default Container;
