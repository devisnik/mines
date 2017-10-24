import { Component } from 'react';

class Tick extends Component {

    componentDidMount() {
        this.interval = setInterval(this.props.onTick, this.props.intervalMs);
    }

    componentWillUnmount() {
        clearInterval(this.interval);
    }

    render() {
        return null;
    }
}

export default Tick;
