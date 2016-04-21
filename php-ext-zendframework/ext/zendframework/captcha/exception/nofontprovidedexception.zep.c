
#ifdef HAVE_CONFIG_H
#include "../../../ext_config.h"
#endif

#include <php.h>
#include "../../../php_ext.h"
#include "../../../ext.h"

#include <Zend/zend_operators.h>
#include <Zend/zend_exceptions.h>
#include <Zend/zend_interfaces.h>

#include "kernel/main.h"


/*

This file is part of the php-ext-zendframework package.

For the full copyright and license information, please view the LICENSE
file that was distributed with this source code.

*/
/**
 * Exception for Zend\Form component.
 */
ZEPHIR_INIT_CLASS(ZendFramework_Captcha_Exception_NoFontProvidedException) {

	ZEPHIR_REGISTER_CLASS_EX(Zend\\Captcha\\Exception, NoFontProvidedException, zendframework, captcha_exception_nofontprovidedexception, zendframework_captcha_exception_invalidargumentexception_ce, NULL, 0);

	return SUCCESS;

}

