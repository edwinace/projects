
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
 * Exception for Zend\Barcode component.
 */
ZEPHIR_INIT_CLASS(ZendFramework_Barcode_Exception_OutOfRangeException) {

	ZEPHIR_REGISTER_CLASS_EX(Zend\\Barcode\\Exception, OutOfRangeException, zendframework, barcode_exception_outofrangeexception, spl_ce_OutOfRangeException, NULL, 0);

	zend_class_implements(zendframework_barcode_exception_outofrangeexception_ce TSRMLS_CC, 1, zendframework_barcode_exception_exceptioninterface_ce);
	return SUCCESS;

}

