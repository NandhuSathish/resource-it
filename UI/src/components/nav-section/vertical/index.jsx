/* eslint-disable react/react-in-jsx-scope */
import PropTypes from 'prop-types';
// @mui
import { styled } from '@mui/material/styles';
import { List, Box, ListSubheader } from '@mui/material';
//
import { NavListRoot } from './NavList';
import { useEffect, useState } from 'react';

// ----------------------------------------------------------------------

export const ListSubheaderStyle = styled((props) => (
  <ListSubheader disableSticky disableGutters {...props} />
))(({ theme }) => ({
  ...theme.typography.overline,
  paddingTop: theme.spacing(3),
  paddingLeft: theme.spacing(2),
  paddingBottom: theme.spacing(1),
  color: theme.palette.text.primary,
  transition: theme.transitions.create('opacity', {
    duration: theme.transitions.duration.shorter,
  }),
}));

// ----------------------------------------------------------------------

NavSectionVertical.propTypes = {
  isCollapse: PropTypes.bool,
  navConfig: PropTypes.array,
};

export default function NavSectionVertical({ navConfig, isCollapse = false, ...other }) {
  const [config, setConfig] = useState([]);

  useEffect(() => {
    const currentRole = parseInt(localStorage.getItem('role'));
    const filteredConfig = navConfig.map((section) => {
      const items = section.items
        .filter((item) => item.roles.includes(currentRole))
        .map((item) => ({
          ...item,
          children: item.children?.filter((child) => child.roles.includes(currentRole)),
        }));

      // If the section's roles include the current role, include the subheader
      if (section.roles?.includes(currentRole)) {
        return {
          ...section,
          items,
        };
      }

      // If the section's roles do not include the current role, exclude the subheader
      return {
        ...section,
        subheader: undefined,
        items,
      };
    });

    setConfig(filteredConfig);
  }, []);
  return (
    <Box {...other}>
      {config.map((group) => (
        <List key={group.subheader} disablePadding sx={{ px: 2 }}>
          <ListSubheaderStyle
            sx={{
              ...(isCollapse && {
                opacity: 0,
              }),
            }}
          >
            {group.subheader}
          </ListSubheaderStyle>

          {group.items.map((list) => (
            <NavListRoot key={list.title} list={list} isCollapse={isCollapse} />
          ))}
        </List>
      ))}
    </Box>
  );
}
