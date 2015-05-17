package slm2015.hey.view.tabs.post;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import slm2015.hey.R;
import slm2015.hey.core.term.TermLoader;
import slm2015.hey.core.term.TermType;
import slm2015.hey.entity.Issue;
import slm2015.hey.view.component.Wizard;
import slm2015.hey.view.component.WizardAdaptor;
import slm2015.hey.view.tabs.TabPagerFragment;

public class PostFragment extends TabPagerFragment {
    private Wizard wizard;
    private ViewPager pager;
    private TermLoader termLoader;
    private Issue issue = new Issue();
    private PreviewFragment previewFragment;

    static public PostFragment newInstance(ViewPager pager) {
        PostFragment fragment = new PostFragment();
        fragment.setPagerSlidingTabStrip(pager);
        return fragment;
    }

    private void setPagerSlidingTabStrip(ViewPager pager) {
        this.pager = pager;
    }

    @Override
    public int getPageIconRedId() {
        return R.drawable.funny_po;
    }

    @Override
    public void FragmentSelected() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.funnypo_layout, container, false);
        this.init(view);
        return view;
    }

    private void init(View rootView) {
        this.findViews(rootView);
        this.initWizard();
        this.initTermLoader();
    }

    private void findViews(View rootView) {
        this.wizard = ((Wizard) rootView.findViewById(R.id.post_wizard));
    }

    private void initTermLoader() {
        this.termLoader = new TermLoader(getActivity());
        this.termLoader.loadRecommends();
    }

    private void initWizard() {
        this.wizard.setAdaptor(new WizardAdaptor(getFragmentManager()) {
            private String[] indicateTexts = {"主角", "描述", "地點", "預覽"};
            private TermType[] types = {TermType.SUBJECT, TermType.DESCRIPTION, TermType.PLACE};

            @Override
            public String getStepIndicateText(int stepIndex) {
                return indicateTexts[stepIndex];
            }

            @Override
            public Fragment getItem(int position) {
                if (position < types.length) {
                    final PostStepFragment postStepFragment = PostStepFragment.newInstance(wizard, PostFragment.this.termLoader, types[position]);
                    postStepFragment.setOnStepFinishListener(new PostStepFragment.OnStepFinishListener() {
                        @Override
                        public void OnStepFinish(String selectedTerm) {
                            //todo implement recode term and prepare to render on preview
                            switch (postStepFragment.getTermType()) {
                                case SUBJECT:
                                    issue.setSubject(selectedTerm);
                                    break;
                                case DESCRIPTION:
                                    issue.setDescription(selectedTerm);
                                    break;
                                case PLACE:
                                    issue.setPlace(selectedTerm);
                                    if (previewFragment != null) {
                                        previewFragment.reassignCard(issue);
                                    }
                                    break;
                            }
                        }
                    });
                    return postStepFragment;
                } else {
                    previewFragment = PreviewFragment.newInstance(issue, wizard);
                    previewFragment.setOnPreviewFinishListener(new PreviewFragment.OnPreviewFinishListener() {
                        @Override
                        public void OnPreviewFinish() {
                            PostFragment.this.pager.setCurrentItem(0);
                            initWizard();
                        }
                    });
                    return previewFragment;
                }
            }

            @Override
            public int getActualCount() {
                return indicateTexts.length;
            }
        });
    }
}