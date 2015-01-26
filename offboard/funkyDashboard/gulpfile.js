var gulp = require('gulp');
var sass = require('gulp-ruby-sass');

var bin = './client/bin/';

gulp.task('js', function() {
  gulp.src(['./client/js/' + '*.js'])
  .pipe(gulp.dest(bin + 'js'));
});

gulp.task('sass', function () {
  gulp.src('./client/bower_components/materialize/font/**')
    .pipe(gulp.dest('./client/bin/font'));

  sass('./client/sass/main.scss')
  .on('error', function (err) { console.log(err.message); })
  .pipe(gulp.dest(bin + 'css'));
});

gulp.task('build', ['js', 'sass']);

gulp.task('watch', [], function() {
  gulp.watch(
    './client/js/**/*.js',
    { interval: 1000, mode: 'poll' },
    ['js']
  );

  gulp.watch(
    './client/sass/**/*.scss',
    { interval: 1000, mode: 'poll' },
    ['sass']
  )
});

gulp.task('default', ['build', 'watch']);
