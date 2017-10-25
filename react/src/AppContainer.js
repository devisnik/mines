import { connect } from 'react-redux';
import App from './App';

const mapStateToProps = (state) => {
    return {
        status: state.status,
        board:  state.board,
        bombCounter: state.bombCounter,
        timeCounter: state.timeCounter,
        isRunning: state.isRunning
    }
}

const AppContainer = connect(mapStateToProps)(App)

export default AppContainer;
