# funkyDashboard

## Setup
1. Install [Node.js](http://nodejs.org/download/)
2. Install Gulp: `npm install --global gulp`
3. Install Bower: `npm install --global bower`
4. Make sure [Ruby](https://www.ruby-lang.org) is installed on your computer
5. Install the Sass compiler: `gem install sass`
6. Install Node packages: `npm install`
7. Install Bower packages: `bower install`

## Building all the things
Run `gulp`. It will automatically watch for changes and rebuild when they are detected.

To test, copy `dashboard-bin` into your home directory. Go to the `DashboardLogger` class in the robot code, comment and uncomment the necessary code, and run it as a Java application.

## Committing
**Do NOT commit the following folders into SVN**:
1. `.saas-cache`
2. `bower_components`
3. `dashboard-bin`
4. `node_modules`
