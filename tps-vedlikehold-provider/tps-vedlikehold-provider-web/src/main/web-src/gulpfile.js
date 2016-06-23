var gulp = require('gulp');
var less = require('gulp-less');
var browserify = require('gulp-browserify');
var source = require('vinyl-source-stream');
var buffer = require('vinyl-buffer');
var jshint = require('gulp-jshint');
var browserSync = require('browser-sync').create();
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var environments = require('gulp-environments');
var connect = require('gulp-connect');
var sourcemaps = require('gulp-sourcemaps');
var cssnano = require('gulp-cssnano');
var cachebust = require('gulp-cache-bust');
var livereload = require('gulp-livereload');

var folders = {
   distRoot : './../resources/public',
   targetRoot: './../../../target/classes/public'
};

var sources = {
   scriptsVendor : './src/assets/lib/*.js',
   scripts: './src/app/**/*.js',
   views : './src/**/*.html',
   styles : './src/assets/less/**/*.less',
   lessMain: './src/assets/less/master.less'
};

gulp.task('scripts', function(){
   return gulp.src([sources.scriptsVendor])
       .pipe(sourcemaps.init())
       .pipe(concat('vendor.min.js'))
       //  .pipe(uglify())
       .pipe(sourcemaps.write())
       .pipe(gulp.dest(folders.distRoot));
});

gulp.task('lint', function() {
   return gulp.src('./src/app/**/*.js')
       .pipe(jshint())
       .pipe(jshint.reporter('default'));
});

gulp.task('browserify', function() {

   return gulp.src('./src/app/app.js')
       .pipe(sourcemaps.init())
       .pipe(browserify( {
          insertGlobals: true,
          debug: true
       }))
       .pipe(concat('main.js'))
       // .pipe(uglify())
       .pipe(sourcemaps.write())
       .pipe(gulp.dest(folders.distRoot))
       .pipe(gulp.dest(folders.targetRoot));
});

gulp.task('views', function() {
   gulp.src([sources.views ])
       .pipe(gulp.dest(folders.distRoot))
       .pipe(gulp.dest(folders.targetRoot));
});

gulp.task('less', function() {
   gulp.src(sources.lessMain)
       .pipe(sourcemaps.init())
       .pipe(less())
       .pipe(cssnano())
       .pipe(concat('app.css'))
       .pipe(sourcemaps.write())
       .pipe(gulp.dest(folders.distRoot))
       .pipe(gulp.dest(folders.targetRoot))
       .pipe(livereload());

});

gulp.task('browser-sync', ['build'], function() {
   browserSync.init({
      server: {
         baseDir: folders.distRoot,
         // The key is the url to match
         // The value is which folder to serve (relative to your current working directory)
         routes: {
            "/bower_components": "bower_components",
            "/node_modules": "node_modules"
         }
      },
      browser:"chrome"
   });
});

gulp.task('connect', function(){
   connect.server({
      root: folders.distRoot,
      livereload: true
   });
});

gulp.task('copy-lib', function(){
   return gulp.src('src/assets/lib/**/*.*')
       .pipe(gulp.dest(folders.distRoot+'/lib'))
       .pipe(gulp.dest(folders.targetRoot+'/lib'));
});

gulp.task('build',['lint', 'less', 'copy-lib', 'scripts', 'browserify', 'views']);


gulp.task('watch', function(){
   livereload.listen();
   gulp.watch(sources.scripts, ['browserify']);
   gulp.watch(sources.styles, ['less']);
   gulp.watch(sources.views, ['views']);
});


gulp.task('watch-dist', function(){
   gulp.watch(folders.distRoot+"/**/*.*").on('change', function(file) {
      gulp.src(file.path)
          .pipe(connect.reload());
   });
});

gulp.task('br', ['browser-sync'], function(){
	gulp.watch("./src/**/*.*", ["build"]);
	gulp.watch("./../Resources/public/**/*.*").on('change', browserSync.reload);
});

gulp.task('default', ['build', 'watch', 'watch-dist', 'br']);




