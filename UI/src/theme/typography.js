import { pxToRem, responsiveFontSizes } from '../utils/getFontValue';

// ----------------------------------------------------------------------

const FONT_PRIMARY = 'Public Sans, sans-serif'; // Google Font
// const FONT_SECONDARY = 'CircularStd, sans-serif'; // Local Font

const typography = {
  fontFamily: FONT_PRIMARY,
  fontWeightRegular: 400,
  fontWeightMedium: 600,
  fontWeightBold: 700,
  h1: {
    fontWeight: 700,
    lineHeight: 80 / 64,
    fontSize: pxToRem(40),
    letterSpacing: 2,
    ...responsiveFontSizes({ sm: 52, md: 58, lg: 56, xl: 64 }),
  },
  h2: {
    fontWeight: 700,
    lineHeight: 64 / 48,
    fontSize: pxToRem(32),
    ...responsiveFontSizes({ sm: 40, md: 44, lg: 42, xl: 48 }),
  },
  h3: {
    fontWeight: 700,
    lineHeight: 1.5,
    fontSize: pxToRem(24),
    ...responsiveFontSizes({ sm: 26, md: 30, lg: 28, xl: 32 }),
  },
  h4: {
    fontWeight: 700,
    lineHeight: 1.5,
    fontSize: pxToRem(20),
    ...responsiveFontSizes({ sm: 20, md: 24, lg: 22, xl: 24 }),
  },
  h5: {
    fontWeight: 700,
    lineHeight: 1.5,
    fontSize: pxToRem(18),
    ...responsiveFontSizes({ sm: 19, md: 20, lg: 18, xl: 20 }),
  },
  h6: {
    fontWeight: 700,
    lineHeight: 28 / 18,
    fontSize: pxToRem(17),
    ...responsiveFontSizes({ sm: 18, md: 18, lg: 12, xl: 18 }),
  },
  subtitle1: {
    fontWeight: 600,
    lineHeight: 1.5,
    fontSize: pxToRem(16),
    ...responsiveFontSizes({ sm: 16, md: 16, lg: 14, xl: 16 }),
  },
  subtitle2: {
    fontWeight: 600,
    lineHeight: 22 / 14,
    fontSize: pxToRem(14),
    fontFamily: FONT_PRIMARY,
    ...responsiveFontSizes({ sm: 14, md: 14, lg: 12, xl: 14 }),
  },
  gridCell: {
    fontWeight: 400,
    lineHeight: 22 / 14,
    fontSize: pxToRem(14),
    ...responsiveFontSizes({ sm: 14, md: 14, lg: 12, xl: 14 }),
  },
  body1: {
    lineHeight: 1.5,
    fontSize: pxToRem(16),
    ...responsiveFontSizes({ sm: 16, md: 16, lg: 14, xl: 16 }),
  },
  body2: {
    lineHeight: 22 / 14,
    fontSize: pxToRem(14),
    ...responsiveFontSizes({ sm: 14, md: 14, lg: 12, xl: 14 }),
  },
  caption: {
    lineHeight: 1.5,
    fontSize: pxToRem(12),
    ...responsiveFontSizes({ sm: 12, md: 12, lg: 10, xl: 12 }),
  },
  overline: {
    fontWeight: 700,
    lineHeight: 1.5,
    fontSize: pxToRem(12),
    ...responsiveFontSizes({ sm: 12, md: 12, lg: 10, xl: 12 }),
    textTransform: 'uppercase',
  },
  button: {
    fontWeight: 700,
    lineHeight: 24 / 14,
    fontSize: pxToRem(14),
    ...responsiveFontSizes({ sm: 14, md: 14, lg: 12, xl: 14 }),
    textTransform: 'capitalize',
  },
};

export default typography;
