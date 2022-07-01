function addDarkmodeWidget() {
    new Darkmode().showWidget();
  }
  window.addEventListener('load', addDarkmodeWidget);
  
  const options = {
  /*①*/ bottom: '64px', // default: '32px'
  /*②*/ right: 'unset', // default: '32px'
  /*③*/ left: '32px', // default: 'unset'
  /*④*/ time: '0.5s', // default: '0.3s'
  /*⑤*/ mixColor: '#fff', // default: '#fff'
  /*⑥*/ backgroundColor: '#fff',  // default: '#fff'
  /*⑦*/ buttonColorDark: '#100f2c',  // default: '#100f2c'
  /*⑧*/ buttonColorLight: '#fff', // default: '#fff'
  saveInCookies: true, // default: true,
  label: '🌓', // default: ''
  autoMatchOsTheme: true // default: true
}

const darkmode = new Darkmode(options);
darkmode.showWidget();