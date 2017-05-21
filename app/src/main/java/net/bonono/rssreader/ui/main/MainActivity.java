package net.bonono.rssreader.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bonono.rssreader.R;
import net.bonono.rssreader.entity.Entry;
import net.bonono.rssreader.entity.Site;
import net.bonono.rssreader.repository.realm.EntryRepository;
import net.bonono.rssreader.repository.realm.SiteRepository;

import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmModel;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainContract.Presenter mPresenter;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(this, new SiteRepository(), new EntryRepository());
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_root);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);

        mPresenter.loadDefaultSite();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    @Override
    public void show(Site site) {
        closeDrawer();

        mToolbar.setTitle(site.getTitle());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.entry_list, EntryListFragment.newInstance(site, EntryRepository.Filter.All))
                .commit();
    }

    @Override
    public void showNoSite() {

    }

    @Override
    public void showEntryDetail(Entry entry) {
        // dummy
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(entry.getUrl())));
    }

    @Override
    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }

    @Override
    public MainContract.Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onClickEntry(Entry entry) {
        mPresenter.activateEntry(entry);
    }
}
