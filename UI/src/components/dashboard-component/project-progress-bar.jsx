/* eslint-disable react/react-in-jsx-scope */

import AnimatedProgressProvider from './AnimatedProgressProvider';
import { CircularProgressbar } from 'react-circular-progressbar';
import { easeQuadIn } from 'd3-ease';

export default function ProjectProgressBar(content) {
  const percentage = content.percentage;

  return (
    <AnimatedProgressProvider
      valueStart={0}
      valueEnd={percentage}
      duration={2}
      easingFunction={easeQuadIn}
    >
      {(value) => {
        const roundedValue = Math.round(value);
        let color;
        if (roundedValue < 50) {
          color = 'red';
        } else if (roundedValue < 75) {
          color = 'orange';
        } else {
          color = 'green';
        }
        return (
          <div style={{ width: content.width, height: content.height }}>
            <CircularProgressbar
              value={value}
              text={`${roundedValue}%`}
              styles={{
                root: {},
                path: {
                  stroke: color,
                  strokeLinecap: 'round',
                },
                trail: {
                  stroke: '#d6d6d6',
                  strokeLinecap: 'round',
                  transform: 'rotate(0.25turn)',
                  transformOrigin: 'center center',
                },
                text: {
                  fill: color,
                  fontSize: '20px',
                  textAnchor: 'middle',
                  dominantBaseline: 'middle',
                },
                background: {
                  fill: '#00000',
                },
              }}
            />
          </div>
        );
      }}
    </AnimatedProgressProvider>
  );
}
