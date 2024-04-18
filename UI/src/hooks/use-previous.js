import { useEffect, useRef } from 'react';

const usePrevious = (value) => {
  const prevLocRef = useRef(value);

  useEffect(() => {
    prevLocRef.current = location;
  }, [location]);

  return prevLocRef.current;
};

export default usePrevious;
