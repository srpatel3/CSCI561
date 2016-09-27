function doSearchInit(){
window.addEvent('domready', function() {
	if (!Browser.Engine.webkit) {
		var input = $('search');
		var placeholder = 'Search';
		
		var setDefaults = function() {
			input.set('value', placeholder);
			input.setStyle('color', '#aaa');	
		}
		
		setDefaults();
		
		input.addEvents({
			'focus': function() {
				if (this.get('value') === placeholder) {
					this.set('value', '');
					this.setStyle('color', 'inherit');
				}
			},
			'blur': function() {
				if (this.get('value') === '') {
					setDefaults();
				}
			} 
		});
	}	
});
}