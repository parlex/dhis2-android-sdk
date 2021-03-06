/*
 * Copyright (c) 2016, University of Oslo
 *
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.client.sdk.android.program;


import org.hisp.dhis.client.sdk.core.program.IProgramController;
import org.hisp.dhis.client.sdk.core.program.IProgramIndicatorService;
import org.hisp.dhis.client.sdk.core.program.IProgramService;
import org.hisp.dhis.client.sdk.core.program.IProgramStageDataElementService;
import org.hisp.dhis.client.sdk.core.program.IProgramStageSectionService;
import org.hisp.dhis.client.sdk.core.program.IProgramStageService;
import org.hisp.dhis.client.sdk.core.program.IProgramTrackedEntityAttributeService;
import org.hisp.dhis.client.sdk.models.program.Program;
import org.hisp.dhis.client.sdk.models.program.ProgramIndicator;
import org.hisp.dhis.client.sdk.models.program.ProgramStage;
import org.hisp.dhis.client.sdk.models.program.ProgramStageDataElement;
import org.hisp.dhis.client.sdk.models.program.ProgramStageSection;
import org.hisp.dhis.client.sdk.models.program.ProgramTrackedEntityAttribute;

import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;

public class ProgramScope implements IProgramScope {

    private final IProgramService mProgramService;
    private final IProgramController mProgramController;
    private final IProgramStageService mProgramStageService;
    private final IProgramStageSectionService mProgramStageSectionService;
    private final IProgramIndicatorService mProgramIndicatorService;
    private final IProgramStageDataElementService mProgramStageDataElementService;
    private final IProgramTrackedEntityAttributeService mProgramTrackedEntityAttributeService;

    public ProgramScope(IProgramService mProgramService, IProgramController mProgramController, IProgramStageService mProgramStageService, IProgramStageSectionService mProgramStageSectionService, IProgramIndicatorService mProgramIndicatorService, IProgramStageDataElementService mProgramStageDataElementService, IProgramTrackedEntityAttributeService mProgramTrackedEntityAttributeService) {
        this.mProgramService = mProgramService;
        this.mProgramController = mProgramController;
        this.mProgramStageService = mProgramStageService;
        this.mProgramStageSectionService = mProgramStageSectionService;
        this.mProgramIndicatorService = mProgramIndicatorService;
        this.mProgramStageDataElementService = mProgramStageDataElementService;
        this.mProgramTrackedEntityAttributeService = mProgramTrackedEntityAttributeService;
    }

    @Override
    public Observable<Boolean> save(final Program program) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    boolean status = mProgramService.save(program);
                    subscriber.onNext(status);
                } catch (Throwable throwable) {
                    subscriber.onError(throwable);
                }

                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Boolean> remove(final Program program) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    boolean status = mProgramService.remove(program);
                    subscriber.onNext(status);
                } catch (Throwable throwable) {
                    subscriber.onError(throwable);
                }

                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Program> get(final long id) {
        return Observable.create(new Observable.OnSubscribe<Program>() {
            @Override
            public void call(Subscriber<? super Program> subscriber) {
                try {
                    Program program = mProgramService.get(id);
                    subscriber.onNext(program);
                } catch (Throwable throwable) {
                    subscriber.onError(throwable);
                }

                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Program> get(final String uid) {
        return Observable.create(new Observable.OnSubscribe<Program>() {
            @Override
            public void call(Subscriber<? super Program> subscriber) {
                try {
                    Program program = mProgramService.get(uid);
                    subscriber.onNext(program);
                } catch (Throwable throwable) {
                    subscriber.onError(throwable);
                }

                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<Program>> list() {
        return Observable.create(new Observable.OnSubscribe<List<Program>>() {
            @Override
            public void call(Subscriber<? super List<Program>> subscriber) {
                try {
                    List<Program> programs = mProgramService.list();
                    subscriber.onNext(programs);
                } catch (Throwable throwable) {
                    subscriber.onError(throwable);
                }

                subscriber.onCompleted();
            }
        });
    }

    @Override
    public void sync() {
        mProgramController.sync();
    }

    @Override
    public void sync(Set<String> uids) {
        mProgramController.sync(uids);
    }

    @Override
    public void sync(String uid) {
        mProgramController.sync();
    }
}
