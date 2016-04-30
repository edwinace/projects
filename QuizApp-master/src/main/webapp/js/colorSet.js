(function(){
	QuizApp.app.config(function($mdThemingProvider) {
		$mdThemingProvider.theme('default')
			.primaryPalette('indigo', {
				'default': '500', // by default use shade 500 from the pink palette for primary intentions
				'hue-1': '200', // use shade 100 for the <code>md-hue-1</code> class
				'hue-2': '900',
				'hue-3': 'A400'
			})
			.accentPalette('orange', {
				'default': '300'
			})
			.warnPalette('pink', {
				'default': 'A400'
			})
	});
})();