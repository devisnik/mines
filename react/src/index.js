import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import AppContainer from './AppContainer';
import './index.css';

import { createStore } from 'redux';
import minesApp from './reducers';

let store = createStore(minesApp);

ReactDOM.render(
    <Provider store={store}>
        <AppContainer />
    </Provider>,
  document.getElementById('root')
);
