var gulp = require('gulp');
var karma = require('karma').server;
var source = require('vinyl-source-stream');
var browserify = require('browserify');
var uglify = require('gulp-uglify');
var buffer = require('vinyl-buffer');
var sourcemaps = require('gulp-sourcemaps');
var jshint = require('gulp-jshint');
var del = require('del');

/******************************************************************************
*  
* Test tasks
*
******************************************************************************/

gulp.task('test:build', function() {
  return browserify({entries: './test/spec/suite.js', debug: true})
         .transform('reactify', {"es6": true})
         .bundle()
         .pipe(source('test-all.js'))
         .pipe(buffer())
         .pipe(sourcemaps.init({loadMaps: true}))
         .pipe(uglify())
         .pipe(sourcemaps.write('./'))
         .pipe(gulp.dest('tmp'));
});

gulp.task('test:run', ['test:build'], function (done) {
  karma.start({
    configFile: __dirname + '/karma.conf.js'
  }, done);
});

gulp.task('test', ['test:run'], function (cb) {
  del(['tmp'], cb);
});

/******************************************************************************
*  
* Build task
*
******************************************************************************/

gulp.task('build', function() {
  return browserify({entries: './src/app.js', debug: true})
         .transform('reactify', {"es6": true})
         .bundle()
         .pipe(source('app.min.js'))
         .pipe(buffer())
         .pipe(sourcemaps.init({loadMaps: true}))
         .pipe(uglify())
         .pipe(sourcemaps.write('./'))
         .pipe(gulp.dest('dist'));
});

/******************************************************************************
*  
* Lint tasks
*
******************************************************************************/

gulp.task('lint', function() {
  return gulp.src(['src/**/*.js', 'test/**/*.js'])
         .pipe(jshint('.jshintrc'))
         .pipe(jshint.reporter('jshint-stylish'));
});