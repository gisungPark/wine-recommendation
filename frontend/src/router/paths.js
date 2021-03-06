import store from '../store';

export default [
  {
    path: '',
    view: 'Main',
    name: 'Main',
  },
  {
    path: '/mypage',
    view: 'Mypage',
    name: 'Mypage',
    beforeEnter: (to, from, next) => {
      if (store.userInfo.userInfo.token != null) {
        return next();
      }
      next('/login');
    },
  },
  {
    path: '/recommendation',
    view: 'Recommendation',
    name: 'Recommendation',
  },
  {
    path: '/wines',
    view: 'Wines',
    name: 'Wines',
  },
  {
    path: '/detail/:id',
    view: 'WineDetails',
    name: 'WineDetials',
  },
  {
    path: '/toptic',
    view: 'Topic',
    name: 'Topic',
  },
];
