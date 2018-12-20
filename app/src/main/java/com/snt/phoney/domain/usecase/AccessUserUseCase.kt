package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.accessor.UserAccessor
import com.snt.phoney.domain.repository.UserRepository

open abstract class AccessUserUseCase(repository: UserRepository) : UserAccessor by repository