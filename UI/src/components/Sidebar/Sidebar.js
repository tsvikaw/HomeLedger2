import React from 'react';
import {connect} from 'react-redux';
import {withRouter, Link} from 'react-router-dom';

import Icon from '../Icon';
import LinksGroup from './LinksGroup/LinksGroup';

import s from './Sidebar.module.scss';

const Sidebar = () => (
  <nav className={s.root}>
    <header className={s.logo}>
      <Link to="/app/main">
        <Icon glyph="logo" />
      </Link>
    </header>
    <ul className={s.nav}>
      <LinksGroup
        header="Dashboard"
        headerLink="/app/main"
        glyph="dashboard"
      />
      <LinksGroup
        header="Expense Details"
        headerLink="/app/query"
        glyph="tables"
      />
      <LinksGroup
        header="Periodic Summary"
        headerLink="/app/components/summaryChart"
        glyph="barchart"
      />
      <LinksGroup
        header="Category Pie"
        headerLink="/app/components/pieChart"
        glyph="piechart"
      />
      <LinksGroup
        header="Import"
        headerLink="/app/import"
        glyph="upload"
      />
      <LinksGroup
        header="Category Editor"
        headerLink="/app/category/editor"
        glyph="upload"
      />
    </ul>
  </nav>
);

function mapStateToProps(store) {
  return {
    sidebarOpened: store.navigation.sidebarOpened,
    sidebarStatic: store.navigation.sidebarStatic,
  };
}

export default withRouter(connect(mapStateToProps)(Sidebar));
