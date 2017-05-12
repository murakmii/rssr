package net.bonono.rssreader.ui.drawer;

import net.bonono.rssreader.entity.Site;
import net.bonono.rssreader.repository.Repository;
import net.bonono.rssreader.repository.realm.SiteRepository;

import java.util.List;

public class DrawerPresenter implements DrawerContract.Presenter {
    private Repository<Site> mRepo;

    public DrawerPresenter() {
        mRepo = new SiteRepository();
    }

    @Override
    public List<Site> loadSites() {
        return mRepo.get(new SiteRepository.All());
    }
}
