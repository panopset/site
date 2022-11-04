package com.panopset.site

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PanBase(
    @Autowired val rc: RedisCache
)
