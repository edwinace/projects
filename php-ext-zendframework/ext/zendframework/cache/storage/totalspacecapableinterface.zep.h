
extern zend_class_entry *zendframework_cache_storage_totalspacecapableinterface_ce;

ZEPHIR_INIT_CLASS(ZendFramework_Cache_Storage_TotalSpaceCapableInterface);

ZEPHIR_INIT_FUNCS(zendframework_cache_storage_totalspacecapableinterface_method_entry) {
	PHP_ABSTRACT_ME(ZendFramework_Cache_Storage_TotalSpaceCapableInterface, getTotalSpace, NULL)
  PHP_FE_END
};
