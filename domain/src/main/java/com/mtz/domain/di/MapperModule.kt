package com.mtz.domain.di

import com.mtz.domain.mapper.ExchangeRateMapper
import com.mtz.domain.mapper.QuoteMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class MapperModule {

    @Provides
    @Singleton
    fun provideExchangeRateMapper(quoteMapper: QuoteMapper): ExchangeRateMapper {
        return ExchangeRateMapper(quoteMapper)
    }

    @Provides
    @Singleton
    fun provideQuoteMapper(): QuoteMapper {
        return QuoteMapper()
    }

}