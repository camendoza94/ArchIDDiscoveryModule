import React from 'react';
import { render, unmountComponentAtNode } from 'react-dom';
import VersionsMeasuresHistoryApp from './components/VersionsMeasuresHistoryApp';
import './style.css';

window.registerExtension('refactoring/architecture', options => {

  const { el } = options;

  render(
          <VersionsMeasuresHistoryApp
            project={options.component}
          />, el
  );

  return () => unmountComponentAtNode(el);
});
