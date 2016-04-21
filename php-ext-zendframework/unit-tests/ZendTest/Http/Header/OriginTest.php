<?php
/**
 * Zend Framework (http://framework.zend.com/)
 *
 * @link      http://github.com/zendframework/zf2 for the canonical source repository
 * @copyright Copyright (c) 2005-2014 Zend Technologies USA Inc. (http://www.zend.com)
 * @license   http://framework.zend.com/license/new-bsd New BSD License
 */

namespace ZendTest\Http\Header;

use Zend\Http\Header\Origin;

class OriginTest extends \PHPUnit_Framework_TestCase
{
    /**
     * @group 6484
     */
    public function testOriginFieldValueIsAlwaysAString()
    {
        $origin = new Origin();

        $this->assertInternalType('string', $origin->getFieldValue());
    }

    public function testOriginFromStringCreatesValidOriginHeader()
    {
        $OriginHeader = Origin::fromString('Origin: http://zend.org');
        $this->assertInstanceOf('Zend\Http\Header\HeaderInterface', $OriginHeader);
        $this->assertInstanceOf('Zend\Http\Header\Origin', $OriginHeader);
    }

    public function testOriginGetFieldNameReturnsHeaderName()
    {
        $OriginHeader = new Origin();
        $this->assertEquals('Origin', $OriginHeader->getFieldName());
    }

    public function testOriginGetFieldValueReturnsProperValue()
    {
        $OriginHeader = Origin::fromString('Origin: http://zend.org');
        $this->assertEquals('http://zend.org', $OriginHeader->getFieldValue());
    }
}
