/**
 * Flatlogic Dashboards (https://flatlogic.com/admin-dashboards)
 *
 * Copyright © 2015-present Flatlogic, LLC. All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

import React from 'react';
import cx from 'classnames';
import { Switch, Route, withRouter } from 'react-router';

import s from './Layout.module.scss';
import Header from '../Header';
import Footer from '../Footer';
import Sidebar from '../Sidebar';

// Dashboard component is loaded directly as an example of server side rendering
import Dashboard from '../../pages/dashboard'
import SummaryChart from '../../pages/charts/summaryChart'
import CategoryPieChart from '../../pages/charts/pieChart'
import NotFound from '../../pages/notFound'
import Query from '../../pages/query'
import Profile from '../../pages/profile'
import Import from '../../pages/import/importPage'
import CategoryEditor from '../../pages/categoryeditor/categoryEditor';

class Layout extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      sidebarOpen: false
    };
  }

  render() {
    return (
      <div className={s.root}>
        <Sidebar />
        <div
          className={cx(s.wrap, {[s.sidebarOpen]: this.state.sidebarOpen})}
        >
          <Header
            sidebarToggle={() =>
              this.setState({
                sidebarOpen: !this.state.sidebarOpen,
              })
            }
          />
          <main className={s.content}>
            <Switch>
              <Route path="/app/main" exact component={Dashboard} />
              <Route path="/app/query" exact component={Query} />
              <Route path="/app/profile" exact component={Profile} />
              <Route path="/app/import" exact component={Import} />
              <Route path="/app/components/summaryChart" exact component={SummaryChart} />
              <Route path="/app/components/pieChart" exact component={CategoryPieChart} />
              <Route path="/app/category/editor" exact component={CategoryEditor} />
              <Route component={NotFound} />
            </Switch>
          </main>
          <Footer />
        </div>
      </div>
    );
  }
}

export default withRouter(Layout);
