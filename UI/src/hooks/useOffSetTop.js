import { useState, useEffect } from 'react';

// ----------------------------------------------------------------------

export default function useOffSetTop(top) {
  const [offsetTop, setOffSetTop] = useState(false);
  const isTop = top || 100;

  useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY > isTop) {
        setOffSetTop(true);
      } else {
        setOffSetTop(false);
      }
    };

    window.addEventListener('scroll', handleScroll);
    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, [isTop]);

  return offsetTop;
}
