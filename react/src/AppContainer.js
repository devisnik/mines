import { connect } from 'react-redux';
import App from './App';
import { openCell, flagCell } from './actions';

const mapStateToProps = (state) => {
    return {
        status: state.status,
        board:  state.board,
        bombCounter: state.bombCounter,
        timeCounter: state.timeCounter,
        isRunning: state.isRunning
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        onCellClicked: (e, x, y) => { 
            if(e.shiftKey) {
                dispatch(openCell(x,y));
            }
            else {
                dispatch(flagCell(x,y));
            }
        }
    }
}

const AppContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(App)

export default AppContainer;
