import React from "react";
import PropTypes from 'prop-types';
import { Animate } from "react-move";

class AnimatedProgressProvider extends React.Component {
    interval = undefined;

    state = {
        isAnimated: false
    };

    static defaultProps = {
        valueStart: 0
    };

    componentDidMount() {
        if (this.props.repeat) {
            this.interval = window.setInterval(() => {
                this.setState({
                    isAnimated: !this.state.isAnimated
                });
            }, this.props.duration * 1000);
        } else {
            this.setState({
                isAnimated: !this.state.isAnimated
            });
        }
    }

    componentWillUnmount() {
        window.clearInterval(this.interval);
    }

    render() {
        return (
            <Animate
                start={() => ({
                    value: this.props.valueStart
                })}
                update={() => ({
                    value: [
                        this.state.isAnimated ? this.props.valueEnd : this.props.valueStart
                    ],
                    timing: {
                        duration: this.props.duration * 1000,
                        ease: this.props.easingFunction
                    }
                })}
            >
                {({ value }) => this.props.children(value)}
            </Animate>
        );
    }
}

AnimatedProgressProvider.propTypes = {
    repeat: PropTypes.bool,
    duration: PropTypes.number.isRequired,
    valueStart: PropTypes.number,
    valueEnd: PropTypes.number.isRequired,
    easingFunction: PropTypes.func.isRequired,
    children: PropTypes.func.isRequired,
};

export default AnimatedProgressProvider;