var gulp = require('gulp');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var sass = require('gulp-ruby-sass');
var minifyCSS = require('gulp-minify-css');

gulp.task('html', function() {
  gulp.src(['./*.html'])
    .pipe(gulp.dest('./dashboard-bin/'));
});

gulp.task('image', function() {
  gulp.src(['./img/*'])
    .pipe(gulp.dest('./dashboard-bin/img/'));
});

gulp.task('datasets', function() {
  gulp.src(['./datasets.json'])
    .pipe(gulp.dest('./dashboard-bin/'));
});

gulp.task('js', function() {
  gulp.src(['./bower_components/jquery/dist/jquery.min.js',
            './bower_components/socket.io-client/socket.io.js',
            './bower_components/flot/jquery.flot.js',
            './bower_components/materialize/bin/materialize.js',
            './js/**/*.js'])
    .pipe(concat('main.min.js'))
    .pipe(uglify())
    .pipe(gulp.dest('./dashboard-bin/js'));
});

gulp.task('font', function() {
  gulp.src('./bower_components/materialize/font/**')
    .pipe(gulp.dest('./dashboard-bin/font'));
});

gulp.task('sass', function() {
  sass('./sass/main.scss')
    .on('error', function (err) { console.log(err.message); })
    .pipe(minifyCSS())
    .pipe(gulp.dest('./dashboard-bin/css'));
});

gulp.task('build', ['html', 'image', 'datasets', 'js', 'font', 'sass']);

gulp.task('watch', [], function() {
  gulp.watch(
    './*.html',
    { interval: 1000, mode: 'poll' },
    ['html']
  );

  gulp.watch(
    './img/*',
    { interval: 1000, mode: 'poll' },
    ['image']
  );

  gulp.watch(
    './datasets.json',
    { interval: 1000, mode: 'poll' },
    ['datasets']
  );

  gulp.watch(
    './js/**/*.js',
    { interval: 1000, mode: 'poll' },
    ['js']
  );

  gulp.watch(
    './sass/**/*.scss',
    { interval: 1000, mode: 'poll' },
    ['sass']
  )
});

gulp.task('default', ['build', 'watch']);
