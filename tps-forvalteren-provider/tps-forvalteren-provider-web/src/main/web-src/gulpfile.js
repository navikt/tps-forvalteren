var gulp = require('gulp');

var browserSync = require('browser-sync').create();

var util = require('gulp-util');
var gulpWebpack = require('gulp-webpack');
var webpack = require('webpack');
var inject = require('gulp-inject');
var jshint = require('gulp-jshint');
var stylish = require('jshint-stylish');

var less = require('gulp-less');
var cssnano = require('gulp-cssnano');
var lessAutoprefix = require('less-plugin-autoprefix');
var hash = require('gulp-hash');
var livereload = require('gulp-livereload');

var del = require('del');
var runSequence = require('run-sequence');


var autoprefix = new lessAutoprefix({
   browsers: ['last 2 versions']
});

var proxyServer = 'localhost:8050';

var folders = {
   distRoot : './../resources/public',
   targetRoot: './../../../target/classes/public',
   distTmp : './temp',
   tempDev: './temp/dev',
   tempProd: './temp/prod'
};

var sources = {
   appEntry: './src/app/app.js',
   lessEntry: './src/styles/master.less',
   scripts: './src/app/**/*.js',
   config: './src/assets/config/**/*.json',
   views : './src/**/*.html',
   styles : './src/styles/**/*.less',
   cssAssets: './src/assets/lib/**/*.css',
   assets: './src/assets/**/*'
};

var tempDest = undefined;

//----- $ npm start
gulp.task('serve.dev', function(){
   tempDest = folders.tempDev;

   runSequence(
       'clean.temp.dev',
       ['clean.target', 'build.app.dev', 'views', 'build.styles.dev', 'copy.assets.dev'],
       'build.index',
       'copy.to.target',
       'watch',
       'start-server'
   );


});

gulp.task('start-server', function() {
    browserSync.init({
        port: 3000,
        proxy: proxyServer,
        injectChanges: true
    })
});

//----- $ npm run build
gulp.task('build.prod', function() {
   util.log("Bunding files for prod");
   tempDest = folders.tempProd;

   runSequence(
       'clean.temp.prod',
       ['clean.public', 'build.app.prod', 'views', 'build.styles.prod', 'copy.assets.prod'],
       'build.index',
       'copy.to.public'
   );
});

// -- JS --
gulp.task('build.app.dev', ['lint'], function() {
   return gulp.src(sources.appEntry)
       .pipe(gulpWebpack({
          output: {
             filename: 'app.js'
          }
       }))
       .pipe(gulp.dest(folders.tempDev))
});

gulp.task('build.app.prod', ['clean.temp.prod', 'lint'], function(){

   return gulp.src(sources.appEntry)
       .pipe(gulpWebpack({
              output: {
                 filename: 'app-[hash].js'
              },
              plugins: [new webpack.optimize.UglifyJsPlugin({
                 output: {
                    comments: false
                 }
              })]
           },
           webpack))
       .pipe(gulp.dest(folders.tempProd));
});

gulp.task('lint', function(){
   gulp.src(sources.scripts)
       .pipe(jshint())
       .pipe(jshint.reporter(stylish))
});


// -- CSS
gulp.task('build.styles.dev', function(){
   return gulp.src(sources.lessEntry)
       // Uncomment to greatly increase compilation time
       // .pipe(sourcemaps.init())
       .pipe(less({
          plugins: [autoprefix]
       }))
       // .pipe(sourcemaps.write())
       .pipe(gulp.dest(tempDest))
       .pipe(livereload())
});

gulp.task('build.styles.prod', function() {
   return gulp.src(sources.lessEntry)
       .pipe(hash({
          hashLength: 20,
          template: '<%= name %>-<%= hash %>.min<%= ext %>'
       }))
       .pipe(less({
          plugins: [autoprefix]
       }))
       .pipe(cssnano())
       .pipe(gulp.dest(tempDest));
});



// -- HTML --
gulp.task('views', function() {
   return gulp.src([sources.views ])
       .pipe(gulp.dest(tempDest));
});

gulp.task('build.index', function(){
   var target = gulp.src(tempDest+'/index.html');
   var sources = gulp.src([tempDest+'/**/*.js', tempDest+'/**/*.css'], {read:false});

   return target.pipe(inject(sources, {relative: true}))
       .pipe(gulp.dest(tempDest));
});


// -- Copy --
gulp.task('copy.to.target', function(){
   return gulp.src(tempDest+'/**/*')
       .pipe(gulp.dest(folders.targetRoot))
});

gulp.task('copy.to.public', function(){
   return gulp.src(tempDest+'/**/*')
       .pipe(gulp.dest(folders.distRoot))
});



// -- Assets --
gulp.task('copy.assets.dev', function() {
   return gulp.src(sources.assets)
       .pipe(gulp.dest(folders.tempDev+'/assets'))

});

gulp.task('copy.assets.prod', function() {
   return gulp.src(sources.assets)
       .pipe(gulp.dest(folders.tempProd+'/assets'))

});



// -- Watch & reload --
gulp.task('watch', function(){
   livereload.listen();
   gulp.watch(sources.scripts, ['reload.scripts']);
   gulp.watch(sources.styles, ['reload.less']);
   gulp.watch(sources.views, ['reload.views']);
   gulp.watch(sources.config, ['reload.config']);
});
gulp.task('reload.scripts', function() {
   runSequence('build.app.dev', 'build.index', 'copy.to.target', 'reload-browser')
});

gulp.task('reload.config', function(){
  runSequence('copy.assets.dev', 'views', 'build.app.dev','build.index','copy.to.target','reload-browser')
});

gulp.task('reload.less', function() {
   runSequence('build.styles.dev', 'copy.to.target')
});

gulp.task('reload.views', function(){
   runSequence('views', 'build.index', 'copy.to.target', 'reload-browser')
});

gulp.task('reload-browser', function(done){
   browserSync.reload();
   done();
});


// -- Clean --
gulp.task('clean.temp.dev', function(cb){
   return del(folders.tempDev, cb);
});

gulp.task('clean.temp.prod', function(cb){
   return del(folders.tempProd, cb);
});

gulp.task('clean.public', function(cb) {
   return del(folders.distRoot, {force:true}, cb);
});

gulp.task('clean.target', function(cb) {
   return del(folders.targetRoot, {force:true}, cb);
});
